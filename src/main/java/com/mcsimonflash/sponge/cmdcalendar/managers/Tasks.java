package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.cmdcalendar.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class Tasks {
    private static List<CmdCalTask> taskList = Lists.newArrayList();

    public static List<CmdCalTask> getTaskList() {
        taskList.sort(Comparator.comparing(CmdCalTask::getName));
        return taskList;
    }

    public static void addTask(String taskName, TaskType taskType) {
        switch (taskType) {
            case Scheduler:
            case GameTime:
                taskList.add(new Scheduler(taskName, taskType));
                break;
            case Interval:
            case GameTick:
                taskList.add(new Interval(taskName, taskType));
                break;
            case UNKNOWN:
                taskList.add(new CmdCalTask(taskName, taskType));
        }
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

    public static void removeTask(CmdCalTask cmdCalTask) {
        taskList.remove(cmdCalTask);
    }

    public static void startTask(CmdCalTask cmdCalTask) {
        RunTask.addTask(cmdCalTask);
        cmdCalTask.setStatus(CmdCalTask.TaskStatus.Active);
    }

    public static void stopTask(CmdCalTask cmdCalTask) {
        RunTask.delTask(cmdCalTask);
        cmdCalTask.setStatus(CmdCalTask.TaskStatus.Halted);
    }
}