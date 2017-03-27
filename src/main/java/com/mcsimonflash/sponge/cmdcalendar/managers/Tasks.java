package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;
import javax.annotation.Nullable;
import java.util.*;

public class Tasks {

    private static Map<String, CmdCalTask> ccTaskMap = new HashMap<>();

    public static TreeMap<String, CmdCalTask> getSortedTaskMap() {
        return new TreeMap<>(ccTaskMap);
    }

    @Nullable
    public static CmdCalTask newTask(String taskName, TaskType type) {
        CmdCalTask ccTask;
        switch (type) {
            case GameTick:
            case Scheduler:
                ccTask = new Scheduler(taskName, type);
                break;
            case GameTime:
            case Interval:
                ccTask = new Interval(taskName, type);
                break;
            default:
                CmdCalendar.getPlugin().getLogger().debug("[DEBUG]: Task type not found");
                return null;
        }
        ccTaskMap.put(taskName, ccTask);
        CmdCalendar.getPlugin().getLogger().debug("[DEBUG]: Adding task to map " + taskName);
        return ccTask;
    }

    public static boolean verifyTask(String taskName) {
        return !(ccTaskMap.get(taskName) == null);
    }

    @Nullable
    public static CmdCalTask getTask(String taskName) {
        return ccTaskMap.get(taskName);
    }

    public static void reloadStatus(String taskName) {
        CmdCalTask ccTask = ccTaskMap.get(taskName);
        if (ccTask instanceof Interval && ((Interval) ccTask).getInterval() != -1 && !ccTask.getCommand().isEmpty()) {
            ccTask.setStatus(TaskStatus.Halted);
        } else if (ccTask instanceof Scheduler && !((Scheduler) ccTask).getSchedule().isEmpty() && !ccTask.getCommand().isEmpty()) {
            ccTask.setStatus(TaskStatus.Halted);
        }
    }

    public static void removeTask(String taskName) {
        ccTaskMap.remove(taskName);
    }

    public static void removeAll() {
        ccTaskMap.clear();
    }

    public static void startTask(String taskName) {
        CmdCalTask ccTask = ccTaskMap.get(taskName);
        RunTask.startTask(ccTask);
        ccTask.setStatus(CmdCalTask.TaskStatus.Active);
    }

    public static void stopTask(String taskName) {
        CmdCalTask ccTask = ccTaskMap.get(taskName);
        RunTask.stopTask(ccTask);
        ccTask.setStatus(CmdCalTask.TaskStatus.Halted);
    }
}