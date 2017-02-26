package com.mcsimonflash.sponge.cmdcalendar;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.io.IOException;
import java.util.Map;

@ConfigSerializable
public class Manager_Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(CmdCalendar.getPlugin().getDefaultConfig())
            .build();
    private static ConfigurationNode rootNode;

    public static boolean readConfig() {
        CmdCalendar.getPlugin().getLogger().info("CmdCal: Config loading...");
        loadConfig();

        Manager_Tasks.getTaskList().clear();

        Map<Object, ? extends ConfigurationNode> taskNameMap = rootNode.getNode("Tasks").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : taskNameMap.entrySet()) {
            boolean addTask = true;
            String taskName = (String) entry.getKey();
            if (Manager_Tasks.verifyTask(taskName)) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: Config contains duplicate tasks named " + taskName + "!");
                addTask = false;
            }
            int taskInterval = rootNode.getNode("Tasks", taskName, "Interval").getInt();
            if (taskInterval < 1) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: " + taskName + " interval is configured improperly!");
                addTask = false;
            }
            String taskCommand = rootNode.getNode("Tasks", taskName, "Command").getString();
            if (!Manager_Commands.verifyUnblockedCommand(taskCommand)) {
                CmdCalendar.getPlugin().getLogger().error("CmdCal: " + taskName + " command is blocked!");
                addTask = false;
            }
            CmdCalendar.getPlugin().getLogger().info("" + addTask);
            if (addTask) {
                Manager_Tasks.addTask(taskName, taskInterval, taskCommand);
                String taskDescription = rootNode.getNode("Tasks", taskName, "Description").getString();
                Manager_Tasks.getTask(taskName).setTaskDescription(taskDescription);
                boolean taskStatus = rootNode.getNode("Tasks", taskName, "Status").getBoolean();
                Manager_Tasks.getTask(taskName).setTaskStatus(taskStatus);
                CmdCalendar.getPlugin().getLogger().info("" + Manager_Tasks.getTaskList().size());
            } else {
                CmdCalendar.getPlugin().getLogger().warn("CmdCal: " + taskName + " could not be loaded!");
            }
            CmdCalendar.getPlugin().getLogger().info(taskName + ", " + taskInterval + ", " + taskCommand + ", " + Manager_Tasks.getTask(taskName).getTaskStatus());
        }

        CmdCalendar.getPlugin().getLogger().info("CmdCal: Config loading complete.");
        return true;
    }

    public static boolean writeConfig() {
        CmdCalendar.getPlugin().getLogger().info("CmdCal: Config saving...");

        for (Object_CmdCalTask task : Manager_Tasks.getTaskList()) {
            rootNode.getNode("Tasks", task.getTaskName(), "Command").setValue(task.getTaskCommand());
            rootNode.getNode("Tasks", task.getTaskName(), "Interval").setValue(task.getTaskInterval());
            rootNode.getNode("Tasks", task.getTaskName(), "Description").setValue(task.getTaskDescription());
            rootNode.getNode("Tasks", task.getTaskName(), "Status").setValue(task.getTaskStatus());
            Manager_Tasks.removeTask(task.getTaskName());
        }

        if (saveConfig()) {
            CmdCalendar.getPlugin().getLogger().info("CmdCal: Config saving complete.");
            return true;
        } else {
            CmdCalendar.getPlugin().getLogger().error("CmdCal: Config could not save!");
            return false;
        }
    }

    public static void loadConfig() {
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveConfig() {
        try {
            loader.save(rootNode);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}