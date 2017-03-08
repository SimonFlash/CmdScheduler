package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigSerializable
public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(CmdCalendar.getPlugin().getDefaultConfig())
            .build();
    private static CommentedConfigurationNode rootNode;

    private static TimeUnit timeUnit;
    public static TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private static boolean ignorePermissionCheck;
    public static boolean isIgnorePermissionCheck() {
        return ignorePermissionCheck;
    }

    private static boolean ignoreBlacklistCheck;
    public static boolean isIgnoreBlacklistCheck() {
        return ignoreBlacklistCheck;
    }

    private static boolean disableRunTasksOnStartup;
    public static boolean isDisableRunTasksOnStartup() {
        return disableRunTasksOnStartup;
    }

    public static boolean readConfig() {
        loadConfig();

        timeUnit = toTimeUnit(rootNode.getNode("Settings", "timeUnit").getString("seconds"));
        ignoreBlacklistCheck = rootNode.getNode("Settings", "ignoreBlacklistCheck").getBoolean(false);
        ignorePermissionCheck = rootNode.getNode("Settings", "ignorePermissionCheck").getBoolean(false);
        disableRunTasksOnStartup = rootNode.getNode("Settings", "disableRunTasksOnStartup").getBoolean(false);

        // String schedulerFormat = rootNode.getNode("Settings", "schedulerFormat").getString("ss mm HH dd MM yyyy");
        // Dates.setSchedulerFormat(Dates.verifySchedulerFormat(schedulerFormat)); SFZ: Implement;

        Map<Object, ? extends ConfigurationNode> taskNameMap = rootNode.getNode("Tasks").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : taskNameMap.entrySet()) {
            String taskName = (String) entry.getKey();
            TaskType taskType = CmdCalTask.parseType(rootNode.getNode("Tasks", taskName, "Type").getString(""));
            boolean addTask = true;

            if (Tasks.verifyTask(taskName)) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " name has multiple instances!");
                addTask = false;
            }
            if (taskType.equals(TaskType.UNKNOWN)) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " type is not valid!");
                addTask = false;
            }

            if (addTask) {
                Tasks.addTask(taskName, taskType);
                CmdCalTask task = Tasks.getTask(taskName);

                if (isDisableRunTasksOnStartup()) {
                    task.setStatus(TaskStatus.Halted);
                } else {
                    task.setStatus(CmdCalTask.parseStatus(rootNode.getNode("Tasks", taskName, "Status").getString("Halted")));
                }

                if (taskType.equals(TaskType.Scheduler) || taskType.equals(TaskType.GameTime)) {
                    String taskSchedule = rootNode.getNode("Tasks", taskName, "Schedule").getString();
                    ((Scheduler) task).setSchedule(taskSchedule);
                    // Validate taskScheduler; SFZ: Implement;
                } else if (taskType.equals(TaskType.Interval) || taskType.equals(TaskType.GameTick)) {
                    int taskInterval = rootNode.getNode("Tasks", taskName, "Interval").getInt(60);
                    if (taskInterval < 1) {
                        CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " interval is below 1!");
                        task.setStatus(TaskStatus.Halted);
                    } else {
                        ((Interval) task).setInterval(taskInterval);
                    }
                }

                String taskCommand = rootNode.getNode("Tasks", taskName, "Command").getString("");
                task.setCommand(taskCommand);
                if (taskCommand.equals("")) {
                    CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " command is not set!");
                    task.setStatus(TaskStatus.Halted);
                } else if (Commands.testCommandExists(taskCommand)) { //SFZ: Implement
                    CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " command is not recognized!");
                    task.setStatus(TaskStatus.Halted);
                } else if (Commands.testCommandBlacklisted(taskCommand)) {
                    CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " command is blacklisted!");
                    task.setStatus(TaskStatus.Halted);
                }

                task.setDescription(rootNode.getNode("Tasks", taskName, "Description").getString(""));
            }
        }

        if (!Tasks.getTaskList().isEmpty()) {
            List<String> loadedTasks = Lists.newArrayList();
            for (CmdCalTask task : Tasks.getTaskList()) {
                loadedTasks.add(task.getName());
            }
            String loadedTaskString = "Loaded " + String.join(", ", loadedTasks) + ".";
            CmdCalendar.getPlugin().getLogger().info(loadedTaskString);
        } else {
            CmdCalendar.getPlugin().getLogger().info("No tasks loaded!");
        }
        return true;
    }

    public static boolean writeConfig() {


        rootNode.getNode("Settings", "timeUnit").setValue(("" + timeUnit).replaceFirst("TimeUnit.", ""));
        rootNode.getNode("Settings", "ignoreBlacklistCheck").setValue(ignoreBlacklistCheck);
        rootNode.getNode("Settings", "ignorePermissionCheck").setValue(ignorePermissionCheck);
        rootNode.getNode("Settings", "disableRunTasksOnStartup").setValue(disableRunTasksOnStartup);

        rootNode.getNode("Settings", "timeUnit").setComment("Time measurement for Interval tasks [Seconds, Minutes, Hours, Days]");
        rootNode.getNode("Settings", "ignoreBlacklist").setComment("Prevent checking the blacklist [CmdCalendar commands, start, stop, restart, op]");
        rootNode.getNode("Settings", "ignorePermissionCheck").setComment("Prevent verification of source permission for commands [Allows users to create a task for any command]");
        rootNode.getNode("Settings", "disableRunTasksOnStartup").setComment("Prevent tasks from running on startup [WARNING: Sets task Status to false for all tasks!]");

        for (CmdCalTask task : Tasks.getTaskList()) {
            String taskName = task.getName();
            rootNode.getNode("Tasks", taskName, "Type").setValue("" + task.getType());
            if (task instanceof Scheduler) {
                Scheduler schedulerTask = (Scheduler) task;
                rootNode.getNode("Tasks", taskName, "Schedule").setValue(schedulerTask.getSchedule());
            } else if (task instanceof Interval) {
                Interval intervalTask = (Interval) task;
                rootNode.getNode("Tasks", taskName, "Interval").setValue(intervalTask.getInterval());
            } else {
                CmdCalendar.getPlugin().getLogger().info("[ERROR]: " + taskName + " TaskType not recognized!");
            }
            rootNode.getNode("Tasks", taskName, "Command").setValue(task.getCommand());
            rootNode.getNode("Tasks", taskName, "Description").setValue(task.getDescription());
            rootNode.getNode("Tasks", taskName, "Status").setValue(task.getStatus());
        }
        Tasks.getTaskList().clear();

        if (saveConfig()) {
            CmdCalendar.getPlugin().getLogger().info("CmdCal: Config successfully saved.");
            return true;
        } else {
            CmdCalendar.getPlugin().getLogger().error("CmdCal: Config could not save!");
            return false;
        }
    }

    private static void loadConfig() {
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean saveConfig() {
        try {
            loader.save(rootNode);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static TimeUnit toTimeUnit(String time) {
        switch (time.toLowerCase()) {
            case "seconds":
                return TimeUnit.SECONDS;
            case "minutes":
                return TimeUnit.MINUTES;
            case "hours":
                return TimeUnit.HOURS;
            case "days":
                return TimeUnit.DAYS;
            default:
                return TimeUnit.SECONDS;
        }
    }
}