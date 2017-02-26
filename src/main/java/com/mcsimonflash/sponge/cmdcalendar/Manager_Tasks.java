package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Manager_Tasks {
    private static List<Object_CmdCalTask> taskList = Lists.newArrayList();

    public static List<Object_CmdCalTask> getTaskList () {
        Collections.sort(taskList, Comparator.comparing(Object_CmdCalTask::getTaskName));
        return taskList;
    }

    public static void clearTaskList () {
        taskList.clear();
    }

    public static boolean verifyTask(String taskName) {
        for (Object_CmdCalTask task : taskList) {
            if (task.getTaskName().equalsIgnoreCase(taskName)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Object_CmdCalTask getTask(String taskName) {
        for (Object_CmdCalTask task : taskList) {
            if (task.getTaskName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }

    public static void addTask(String taskName, int taskInterval, String taskCommand) {
        taskList.add(new Object_CmdCalTask(taskName, taskInterval, taskCommand));
    }

    public static boolean removeTask(String taskName) {
        if (verifyTask(taskName)) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getTaskName().equalsIgnoreCase(taskName)) {
                    taskList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean startTask(String taskName) {
        if (verifyTask(taskName)) {
            if (getTask(taskName).getTaskCommand() != null) {
                getTask(taskName).setTaskStatus(true);
                Handler_RunTask.addTask(getTask(taskName));
                return true;
            }
        }
        return false;
    }

    public static boolean stopTask(String taskName) {
        if (verifyTask(taskName)) {
            getTask(taskName).setTaskStatus(false);
            Handler_RunTask.delTask(getTask(taskName));
            return true;
        }
        return false;
    }

    public static boolean toggleTask(String taskName) {
        if (verifyTask(taskName)) {
            getTask(taskName).setTaskStatus(!getTask(taskName).getTaskStatus());
            if (getTask(taskName).getTaskStatus()) {
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
            getTask(oldTaskName).setTaskName(newTaskName);
            return true;
        }
        return false;
    }

    public static boolean setTaskInterval(String taskName, int taskInterval) {
        if (verifyTask(taskName)) {
            getTask(taskName).setTaskInterval(taskInterval);
            return true;
        }
        return false;
    }

    public static boolean setTaskDescription(String taskName, String taskDescription) {
        if (verifyTask(taskName)) {
            getTask(taskName).setTaskDescription(taskDescription);
            return true;
        }
        return false;
    }

    public static boolean setTaskCommand(String taskName, String taskCommand) {
        if (verifyTask(taskName)) {
            getTask(taskName).setTaskCommand(taskCommand);
            return true;
        }
        return false;
    }
}