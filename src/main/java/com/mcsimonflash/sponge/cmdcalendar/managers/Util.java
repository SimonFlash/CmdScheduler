package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;
import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.IntervalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.SchedulerTask;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class Util {

    /**
     * GENERAL
     * <p>
     * refreshStatus -> Validates the status of task @ccTask
     * getTaskNames -> Returns a List of all task names, sorted if @sort
     * clearMaps -> Clears the task maps
     */

    public static void refreshStatus(CmdCalTask ccTask) {
        if ((ccTask.Command.isEmpty() ||
                ccTask instanceof IntervalTask && ((IntervalTask) ccTask).Interval == -1) ||
                ccTask instanceof SchedulerTask && ((SchedulerTask) ccTask).Schedule.isEmpty()) {
            ccTask.Status = TaskStatus.ERROR;
        } else if (ccTask.Status.equals(TaskStatus.ERROR)) {
            ccTask.Status = TaskStatus.Halted;
        }
    }

    public static List<String> getTaskNames(boolean sort) {
        List<String> taskNames = Lists.newArrayList();
        taskNames.addAll(Tasks.taskMap.keySet());
        if (sort) {
            taskNames.sort(Comparator.naturalOrder());
        }
        return taskNames;
    }

    public static void clearMaps() {
        Tasks.taskMap.clear();
        Tasks.stopAll();
    }

    /**
     * TASK MANAGEMENT
     * <p>
     * createTask -> Creates a task named @name of TaskType @type
     * startTask -> Activates the task @ccTask
     * stopTask -> Halts the task @ccTask
     */

    @Nullable
    public static CmdCalTask createTask(String name, TaskType type) {
        CmdCalTask ccTask;
        switch (type) {
            case GameTick:
            case Scheduler:
                ccTask = new SchedulerTask(name, type);
                break;
            case GameTime:
            case Interval:
                ccTask = new IntervalTask(name, type);
                break;
            default:
                return null;
        }
        Tasks.taskMap.put(ccTask.Name, ccTask);
        return ccTask;
    }

    public static void startTask(CmdCalTask ccTask) {
        ccTask.Status = ccTask.Status.equals(TaskStatus.Halted) ? TaskStatus.Active : TaskStatus.Concealed_Active;
        Tasks.startTask(ccTask);
    }

    public static void stopTask(CmdCalTask ccTask) {
        ccTask.Status = ccTask.Status.equals(TaskStatus.Active) ? TaskStatus.Halted : TaskStatus.Concealed_Halted;
        Tasks.stopTask(ccTask);
    }

    /**
     * VALUE CHECKS
     * <p>
     * validateSchedule -> returns true if the schedule @schedule meets the required format
     * checkBlacklist -> returns true if the command @command is blacklisted
     * checkSchedule -> returns true if the schedule @schedule should run
     */

    public static boolean validateSchedule(String schedule) {
        String[] split = schedule.split(" ");
        for (int i = 0; i < 5; i++) {
            if (split[i].equals("*")) {
                continue;
            }
            if (split[i].length() > 1 && split[i].substring(0, 1).equals("/")) {
                split[i] = split[i].substring(1);
            }
            try {
                int num = Integer.parseInt(split[i]);
                switch (i) {
                    case 0:
                    case 1:
                        if (num < 0 || num > 59) return false;
                        break;
                    case 2:
                        if (num < 0 || num > 23) return false;
                        break;
                    case 3:
                        if (num < 1 || num > Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)) return false;
                        break;
                    case 4:
                        if (num < 1 || num > 12) return false;
                }
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkBlacklist(String command) {
        String coreCommand = command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command;
        return Config.isBlacklistCheck() && Config.getBlacklist().contains(coreCommand.toLowerCase());
    }

    public static boolean checkSchedule(String schedule) {
        String[] splitTaskSchedule = schedule.split(" ");
        Calendar cal = Calendar.getInstance();
        int[] calendarArray = {cal.get(Calendar.SECOND), cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1};
        for (int i = 0; i < 5; i++) {
            if (!splitTaskSchedule[i].substring(0, 1).equals("*")) {
                if (splitTaskSchedule[i].substring(0, 1).equals("/")) {
                    if (calendarArray[i] % Integer.parseInt(splitTaskSchedule[i].substring(1)) != 0) {
                        return false;
                    }
                } else if (calendarArray[i] != Integer.parseInt(splitTaskSchedule[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * PARSING & PRINTING
     * <p>
     * parseStatus -> returns the TaskStatus enum for String @status
     * printStatus -> returns the String value of TaskStatus @taskStatus
     * parseType -> returns the TaskType enum for String @type
     * printType -> returns the String value of TaskType @taskType
     */

    //Convert to valueOf() and name()

    public static TaskStatus parseStatus(String status) {
        switch (status.toLowerCase()) {
            case "active":
                return TaskStatus.Active;
            case "concealed_halted":
                return TaskStatus.Concealed_Halted;
            case "concealed_active":
                return TaskStatus.Concealed_Active;
            case "halted":
                return TaskStatus.Halted;
            case "suspended":
                return TaskStatus.Suspended;
            default:
                return TaskStatus.ERROR;
        }
    }

    public static String printStatus(TaskStatus taskStatus) {
        switch (taskStatus) {
            case Active:
                return "Active";
            case Concealed_Active:
                return "Concealed_Active";
            case Concealed_Halted:
                return "Concealed_Halted";
            case Halted:
                return "Halted";
            case Suspended:
                return "Suspended";
            default:
                return "Error";
        }
    }

    public static TaskType parseType(String type) {
        switch (type.toLowerCase()) {
            case "scheduler":
            case "sched":
            case "s":
                return TaskType.Scheduler;
            case "interval":
            case "inter":
            case "i":
                return TaskType.Interval;
            default:
                return TaskType.ERROR;
        }
    }

    public static String printType(TaskType taskType) {
        switch (taskType) {
            case GameTick:
                return "GameTick";
            case GameTime:
                return "GameTime";
            case Interval:
                return "Interval";
            case Scheduler:
                return "Scheduler";
            default:
                return "Error";
        }
    }
}
