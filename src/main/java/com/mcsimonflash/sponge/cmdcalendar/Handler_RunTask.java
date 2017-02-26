package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.scheduler.Task;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Handler_RunTask {
    private static List<Task> activeTasks = Lists.newArrayList();

    public static void syncTasks() {
        for (Object_CmdCalTask cmdCalTask : Manager_Tasks.getTaskList()) {
            if (cmdCalTask.getTaskStatus()) {
                activeTasks.add(Task.builder().execute(
                        task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getTaskCommand()))
                        .name("CmdCal_RunTask_" + cmdCalTask.getTaskName())
                        .delay(cmdCalTask.getTaskInterval(), TimeUnit.MINUTES)
                        .interval(cmdCalTask.getTaskInterval(), TimeUnit.MINUTES)
                        .async()
                        .submit(CmdCalendar.getPlugin()));
            }
        }
        if (!activeTasks.isEmpty()) {
            List<String> activatedTasks = Lists.newArrayList();
            for (Task task : activeTasks) {
                activatedTasks.add(task.getName());
            }
            String activeTaskString = "Activated" + String.join(", ", activatedTasks) + ".";
            CmdCalendar.getPlugin().getLogger().info(activeTaskString);
        } else {
            CmdCalendar.getPlugin().getLogger().warn("No tasks activated!");
        }
    }

    public static boolean verifyTask(Object_CmdCalTask cmdCalTask) {
        for (Task task : activeTasks) {
            if (task.getName().equals("CmdCal_RunTask_" + cmdCalTask.getTaskName())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Task getTask(Object_CmdCalTask cmdCalTask) {
        for (Task task : activeTasks) {
            if (task.getName().equals("CmdCal_RunTask_" + cmdCalTask.getTaskName())) {
                return task;
            }
        }
        return null;
    }

    public static boolean addTask(Object_CmdCalTask cmdCalTask) {
        if (!verifyTask(cmdCalTask)) {
            if (Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getTaskCommand()).equals(CommandResult.success())) {
                activeTasks.add(Task.builder().execute(
                        task -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdCalTask.getTaskCommand()))
                        .name("CmdCal_RunTask_" + cmdCalTask.getTaskName())
                        .delay(cmdCalTask.getTaskInterval(), TimeUnit.MINUTES)
                        .interval(cmdCalTask.getTaskInterval(), TimeUnit.MINUTES)
                        .async()
                        .submit(CmdCalendar.getPlugin()));
                CmdCalendar.getPlugin().getLogger().info("CmdCal_RunTask_" + cmdCalTask.getTaskName() + "activated.");
                return true;
            }
        }
        return false;
    }

    public static boolean delTask(Object_CmdCalTask cmdCalTask) {
        if (verifyTask(cmdCalTask)) {
            for (int i = 0; i < activeTasks.size(); i++) {
                if (activeTasks.get(i).getName().equalsIgnoreCase("CmdCal_RunTask_" + cmdCalTask.getTaskName())) {
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