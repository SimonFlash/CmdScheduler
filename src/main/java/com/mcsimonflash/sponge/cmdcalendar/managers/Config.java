package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;

import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;

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
        CmdCalendar.getPlugin().getLogger().info("CmdCal: Config loading...");
        loadConfig();

        timeUnit = toTimeUnit(rootNode.getNode("Settings", "timeUnit").getString("seconds"));
        ignoreBlacklistCheck = rootNode.getNode("Settings", "ignoreBlacklistCheck").getBoolean(false);
        ignorePermissionCheck = rootNode.getNode("Settings", "ignorePermissionCheck").getBoolean(false);
        disableRunTasksOnStartup = rootNode.getNode("Settings", "disableRunTasksOnStartup").getBoolean(false);

        Map<Object, ? extends ConfigurationNode> taskNameMap = rootNode.getNode("Tasks").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : taskNameMap.entrySet()) {
            boolean addTask = true;
            String taskName = (String) entry.getKey();
            if (Tasks.verifyTask(taskName)) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: Config contains duplicate tasks named " + taskName + "!");
                addTask = false;
            }
            int taskInterval = rootNode.getNode("Tasks", taskName, "Interval").getInt(60);
            if (taskInterval < 1) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: " + taskName + " interval is configured improperly!");
                addTask = false;
            }
            String taskCommand = rootNode.getNode("Tasks", taskName, "Command").getString("");
            if (!Commands.testCommandNotBlacklisted(taskCommand)) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: " + taskName + " command is blocked!");
                addTask = false;
            }
            if (addTask) {
                Tasks.addTask(taskName, taskInterval, taskCommand);
                String taskDescription = rootNode.getNode("Tasks", taskName, "Description").getString("");
                Tasks.getTask(taskName).setDescription(taskDescription);
                if (Config.isDisableRunTasksOnStartup()) {
                    boolean taskStatus = rootNode.getNode("Tasks", taskName, "Status").getBoolean(false);
                    Tasks.getTask(taskName).setStatus(taskStatus);
                } else {
                    Tasks.getTask(taskName).setStatus(false);
                }
            } else {
                CmdCalendar.getPlugin().getLogger().warn("CmdCal: " + taskName + " could not be loaded!");
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
        rootNode.getNode("Settings", "ignoreBlacklist").setValue(ignoreBlacklistCheck);
        rootNode.getNode("Settings", "ignorePermissionCheck").setValue(ignorePermissionCheck);
        rootNode.getNode("Settings", "disableRunTasksOnStartup").setValue(disableRunTasksOnStartup);

        rootNode.getNode("Settings", "timeUnit").setComment("Interval measurement for tasks [Seconds, Minutes, Hours, Days]");
        rootNode.getNode("Settings", "ignoreBlacklist").setComment("Prevent checking the blacklist [CmdCalendar commands, start, stop, restart, op");
        rootNode.getNode("Settings", "ignorePermissionCheck").setComment("Prevent verification of source permission for commands");
        rootNode.getNode("Settings", "disableRunTasksOnStartup").setComment("Prevent tasks from running on startup [WARNING: Sets task Status to false for all tasks!]");

        for (CmdCalTask task : Tasks.getTaskList()) {
            rootNode.getNode("Tasks", task.getName(), "Command").setValue(task.getCommand());
            rootNode.getNode("Tasks", task.getName(), "Interval").setValue(task.getInterval());
            rootNode.getNode("Tasks", task.getName(), "Description").setValue(task.getDescription());
            rootNode.getNode("Tasks", task.getName(), "Status").setValue(task.getStatus());
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