package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.managers.Commands;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.scheduler.Task;

import javax.annotation.Nullable;
import java.util.List;

public class RunTask {
    private static List<Task> activeTasks = Lists.newArrayList();

    static void setupTasks() {
        CmdCalendar.getPlugin().getLogger().info("CmdCal: Setting up tasks...");
        if (!Config.isDisableRunTasksOnStartup()) {
            for (CmdCalTask cmdCalTask : Tasks.getTaskList()) {
                if (cmdCalTask.getStatus()) {
                    if (Commands.testCommandExists(cmdCalTask.getCommand()))
                    {
                        activeTasks.add(Task.builder().execute(
                                task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getCommand()))
                                .name("CmdCal_RunTask_" + cmdCalTask.getName())
                                .delay(cmdCalTask.getInterval(), Config.getTimeUnit())
                                .interval(cmdCalTask.getInterval(), Config.getTimeUnit())
                                .async()
                                .submit(CmdCalendar.getPlugin()));
                    } else {
                        CmdCalendar.getPlugin().getLogger().info("Task " + cmdCalTask + " has an invalid command!");
                    }
                }
            }
            if (!activeTasks.isEmpty()) {
                List<String> activatedTasks = Lists.newArrayList();
                for (Task task : activeTasks) {
                    activatedTasks.add(task.getName());
                }
                String activeTaskString = "Activated " + String.join(", ", activatedTasks) + ".";
                CmdCalendar.getPlugin().getLogger().info(activeTaskString);
            } else {
                CmdCalendar.getPlugin().getLogger().info("No tasks activated!");
            }
        } else {
            CmdCalendar.getPlugin().getLogger().info("Tasks not run: Config setting disableRunTasksOnStartup = true");
        }
    }

    public static boolean verifyTask(CmdCalTask cmdCalTask) {
        for (Task task : activeTasks) {
            if (task.getName().equals("CmdCal_RunTask_" + cmdCalTask.getName())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Task getTask(CmdCalTask cmdCalTask) {
        for (Task task : activeTasks) {
            if (task.getName().equals("CmdCal_RunTask_" + cmdCalTask.getName())) {
                return task;
            }
        }
        return null;
    }

    public static boolean addTask(CmdCalTask cmdCalTask) {
        if (!verifyTask(cmdCalTask)) {
            if (Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getCommand()).equals(CommandResult.success())) {
                activeTasks.add(Task.builder().execute(
                        task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getCommand()))
                        .name("CmdCal_RunTask_" + cmdCalTask.getName())
                        .delay(cmdCalTask.getInterval(), Config.getTimeUnit())
                        .interval(cmdCalTask.getInterval(), Config.getTimeUnit())
                        .async()
                        .submit(CmdCalendar.getPlugin()));
                CmdCalendar.getPlugin().getLogger().info("CmdCal_RunTask_" + cmdCalTask.getName() + "activated.");
                return true;
            }
        }
        return false;
    }

    public static boolean delTask(CmdCalTask cmdCalTask) {
        if (verifyTask(cmdCalTask)) {
            for (int i = 0; i < activeTasks.size(); i++) {
                if (activeTasks.get(i).getName().equalsIgnoreCase("CmdCal_RunTask_" + cmdCalTask.getName())) {
                    getTask(cmdCalTask).cancel();
                    activeTasks.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static void delAll() {
        for (Task task : activeTasks) {
            task.cancel();
        }
    }
}