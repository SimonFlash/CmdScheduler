package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;
import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.IntervalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.SchedulerTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Tasks {

    public static Map<String, CmdCalTask> taskMap = new HashMap<>();
    private static Map<CmdCalTask, Task> activeTasksMap = new HashMap<>();

    public static void reloadTasks() {
        stopAll();
        List<String> activatedTasks = Lists.newArrayList();
        for (CmdCalTask ccTask : Tasks.taskMap.values()) {
            Util.refreshStatus(ccTask);
            if (ccTask.Status.equals(TaskStatus.Active) || ccTask.Status.equals(TaskStatus.Concealed_Active)) {
                startTask(ccTask);
                activatedTasks.add(ccTask.Name);
            }
        }
        if (activatedTasks.isEmpty()) {
            CmdCalendar.getPlugin().getLogger().info("No tasks activated!");
        } else {
            CmdCalendar.getPlugin().getLogger().info("Activated Tasks: " + String.join(", ", activatedTasks) + ".");
        }
    }

    public static void startTask(CmdCalTask ccTask) {
        if (ccTask instanceof SchedulerTask) {
            activeTasksMap.put(ccTask, Task.builder().execute(
                    task -> {
                        if (Util.checkSchedule(((SchedulerTask) ccTask).Schedule)) {
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ccTask.Command);
                        }
                    })
                    .name("CmdCal_RunTask_" + ccTask.Name)
                    .delay(1, TimeUnit.SECONDS)
                    .interval(1, TimeUnit.SECONDS)
                    .submit(CmdCalendar.getPlugin()));
        } else if (ccTask instanceof IntervalTask) {
            activeTasksMap.put(ccTask, Task.builder().execute(
                    task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ccTask.Command))
                    .name("CmdCal_RunTask_" + ccTask.Name)
                    .delay(Config.isIntervalDelayStart() ? ((IntervalTask) ccTask).Interval : 0, TimeUnit.SECONDS)
                    .interval(((IntervalTask) ccTask).Interval, TimeUnit.SECONDS)
                    .submit(CmdCalendar.getPlugin()));
        }
    }

    public static void stopTask(CmdCalTask ccTask) {
        activeTasksMap.get(ccTask).cancel();
        activeTasksMap.remove(ccTask);
    }

    public static void stopAll() {
        for (CmdCalTask ccTask : activeTasksMap.keySet()) {
            Util.stopTask(ccTask);
        }
        activeTasksMap.clear();
    }

    public static boolean isActive(CmdCalTask ccTask) {
        return activeTasksMap.containsKey(ccTask);
    }

    public static boolean isActiveTasksMapEmpty() {
        return activeTasksMap.isEmpty();
    }
}