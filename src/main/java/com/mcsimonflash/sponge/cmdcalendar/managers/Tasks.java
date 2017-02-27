package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.cmdcalendar.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tasks {
    private static List<CmdCalTask> taskList = Lists.newArrayList();

    public static List<CmdCalTask> getTaskList () {
        Collections.sort(taskList, Comparator.comparing(CmdCalTask::getName));
        return taskList;
    }

    public static void clearTaskList () {
        taskList.clear();
    }

    public static boolean verifyTask(String taskName) {
        for (CmdCalTask task : taskList) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static CmdCalTask getTask(String taskName) {
        for (CmdCalTask task : taskList) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }

    public static void addTask(String taskName, int taskInterval, String taskCommand) {
        taskList.add(new CmdCalTask(taskName, taskInterval, taskCommand));
    }

    public static boolean removeTask(String taskName) {
        if (verifyTask(taskName)) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getName().equalsIgnoreCase(taskName)) {
                    taskList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean startTask(String taskName) {
        if (verifyTask(taskName)) {
            if (getTask(taskName).getCommand() != null) {
                getTask(taskName).setStatus(true);
                RunTask.addTask(getTask(taskName));
                return true;
            }
        }
        return false;
    }

    public static boolean stopTask(String taskName) {
        if (verifyTask(taskName)) {
            getTask(taskName).setStatus(false);
            RunTask.delTask(getTask(taskName));
            return true;
        }
        return false;
    }

    public static boolean toggleTask(String taskName) {
        if (verifyTask(taskName)) {
            getTask(taskName).setStatus(!getTask(taskName).getStatus());
            if (getTask(taskName).getStatus()) {
                startTask(taskName);
                return true;
            } else {
                stopTask(taskName);
                return false;
            }
        }
        return false;
    }

    public static boolean setTaskName(String oldTaskName, String newTaskName) {
        if (verifyTask(oldTaskName) && !verifyTask(newTaskName)) {
            getTask(oldTaskName).setName(newTaskName);
            return true;
        }
        return false;
    }

    public static boolean setTaskInterval(String taskName, int taskInterval) {
        if (verifyTask(taskName)) {
            getTask(taskName).setInterval(taskInterval);
            return true;
        }
        return false;
    }

    public static boolean setTaskDescription(String taskName, String taskDescription) {
        if (verifyTask(taskName)) {
            getTask(taskName).setDescription(taskDescription);
            return true;
        }
        return false;
    }

    public static boolean setTaskCommand(String taskName, String taskCommand) {
        if (verifyTask(taskName)) {
            getTask(taskName).setCommand(taskCommand);
            return true;
        }
        return false;
    }
}