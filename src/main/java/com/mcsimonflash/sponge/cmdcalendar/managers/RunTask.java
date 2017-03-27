package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;
import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RunTask {
    private static Map<CmdCalTask, Task> activeTasksMap = new HashMap<>();

    public static void syncTasks() {
        Tasks.removeAll();
        removeAll();
        if (Config.isActivateOnStartup()) {
            List<String> activatedTasks = Lists.newArrayList();
            for (CmdCalTask ccTask : Tasks.getSortedTaskMap().values()) {
                CmdCalendar.getPlugin().getLogger().error("[DEBUG]: Checking task" + ccTask.getName());
                if (ccTask.getStatus().equals(TaskStatus.Active)) {
                    startTask(ccTask);
                    activatedTasks.add(ccTask.getName());
                }
            }
            if (activatedTasks.isEmpty()) {
                CmdCalendar.getPlugin().getLogger().info("No tasks activated!");
            } else {
                CmdCalendar.getPlugin().getLogger().info("Activated " + String.join(", ", activatedTasks) + ".");
            }
        } else {
            CmdCalendar.getPlugin().getLogger().info("[CmdCal]: Tasks not run: Config activate_on_startup = false");
        }
    }

    public static void startTask(CmdCalTask ccTask) {
        if (ccTask instanceof Scheduler) {
            activeTasksMap.put(ccTask, Task.builder().execute(
                    task -> {
                        String[] splitTaskSchedule = ((Scheduler) ccTask).getSchedule().split(" ");
                        Calendar cal = Calendar.getInstance();
                        int[] calendarArray = {cal.get(Calendar.SECOND), cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1};

                        //SFZ: Implement Conditions;

                        boolean runTask = true;
                        for (int i = 0; i < 5; i++) {
                            if (!splitTaskSchedule[i].substring(0, 1).equals("*")) {
                                if (splitTaskSchedule[i].substring(0, 1).equals("/")) {
                                    if (calendarArray[i] % Integer.parseInt(splitTaskSchedule[i].substring(1)) != 0) {
                                        runTask = false;
                                    }
                                } else if (calendarArray[i] != Integer.parseInt(splitTaskSchedule[i])) {
                                    runTask = false;
                                }
                            }
                        }

                        if (runTask) {
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ccTask.getCommand());
                        }
                    })
                    .name("CmdCal_RunTask_" + ccTask.getName())
                    .delay(1, TimeUnit.SECONDS)
                    .interval(1, TimeUnit.SECONDS)
                    .submit(CmdCalendar.getPlugin()));
        } else if (ccTask instanceof Interval) {
            int taskInterval = ((Interval) ccTask).getInterval();
            int delayInterval = Config.isIntervalDelayStart() ? taskInterval : 0;
            if (ccTask.getType().equals(TaskType.Interval)) {
                activeTasksMap.put(ccTask, Task.builder().execute(
                        task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ccTask.getCommand()))
                        .name("CmdCal_RunTask_" + ccTask.getName())
                        .delay(delayInterval, Config.getIntervalTimeUnit())
                        .interval(taskInterval, Config.getIntervalTimeUnit())
                        .submit(CmdCalendar.getPlugin()));
            } else if (ccTask.getType().equals(TaskType.GameTick)) {
                activeTasksMap.put(ccTask, Task.builder().execute(
                        task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ccTask.getCommand()))
                        .name("CmdCal_RunTask_" + ccTask.getName())
                        .delayTicks(delayInterval)
                        .intervalTicks(taskInterval)
                        .submit(CmdCalendar.getPlugin()));
            }
        }
    }

    public static void stopTask(CmdCalTask ccTask) {
        activeTasksMap.get(ccTask).cancel();
        activeTasksMap.remove(ccTask);
    }

    public static boolean isActive(CmdCalTask ccTask) {
        return activeTasksMap.containsKey(ccTask);
    }

    public static void removeTask(CmdCalTask ccTask) {
        activeTasksMap.remove(ccTask);
    }

    public static void removeAll() {
        activeTasksMap.clear();
    }
}