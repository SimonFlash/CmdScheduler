package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.IntervalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.SchedulerTask;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(CmdCalendar.getPlugin().getDefaultConfig()).build();
    private static CommentedConfigurationNode rootNode;

    public static void readConfig() {
        if (Files.notExists(CmdCalendar.getPlugin().getDefaultConfig())) {
            try {
                Sponge.getAssetManager().getAsset(CmdCalendar.getPlugin(), "defaultConfig.conf").get().copyToFile(CmdCalendar.getPlugin().getDefaultConfig());
                CmdCalendar.getPlugin().getLogger().warn("Default Config loaded! Edit cmdcalendar.conf to change settings and create tasks!");
            } catch (IOException e) {
                e.printStackTrace();
                CmdCalendar.getPlugin().getLogger().error("Unable to clone asset! Default config must be copied in manually");
                return;
            }
        }
        loadConfig();
        Util.clearMaps();
        List<String> loadedTasks = Lists.newArrayList();
        Map<Object, ? extends ConfigurationNode> taskNameMap = rootNode.getNode("tasks").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> task : taskNameMap.entrySet()) {
            String name = (String) task.getKey();
            if (Tasks.taskMap.containsKey(name)) {
                Tasks.taskMap.remove(name);
                CmdCalendar.getPlugin().getLogger().error(name + " name has multiple instances!");
                continue;
            }
            TaskType type = Util.parseType(rootNode.getNode("tasks", name, "type").getString(""));
            if (type.equals(TaskType.ERROR)) {
                CmdCalendar.getPlugin().getLogger().error(name + " type is not valid!");
                continue;
            }
            CmdCalTask ccTask = Util.createTask(name, type);
            loadTask(ccTask);
            loadedTasks.add(ccTask.Name);
        }

        if (loadedTasks.isEmpty()) {
            CmdCalendar.getPlugin().getLogger().info("No tasks loaded!");
        } else {
            loadedTasks.sort(Comparator.naturalOrder());
            CmdCalendar.getPlugin().getLogger().info("Loaded Tasks: " + String.join(", ", loadedTasks) + ".");
            if (isActivateOnStartup()) {
                Tasks.reloadTasks();
            }
        }
    }

    public static void loadTask(CmdCalTask ccTask) {
        ccTask.Status = Util.parseStatus(rootNode.getNode("tasks", ccTask.Name, "status").getString("Error"));
        if (!isActivateOnStartup()) {
            if (ccTask.Status.equals(TaskStatus.Active)) {
                ccTask.Status = TaskStatus.Halted;
            } else if (ccTask.Status.equals(TaskStatus.Concealed_Active)) {
                ccTask.Status = TaskStatus.Concealed_Halted;
            }
        }
        if (ccTask instanceof SchedulerTask) {
            ((SchedulerTask) ccTask).Schedule = rootNode.getNode("tasks", ccTask.Name, "schedule").getString("");
            if (((SchedulerTask) ccTask).Schedule.isEmpty()) {
                CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " schedule is not set!");
                ccTask.Status = TaskStatus.ERROR;
            } else if (((SchedulerTask) ccTask).Schedule.length() - ((SchedulerTask) ccTask).Schedule.replace(" ", "").length() != 4) {
                CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " schedule has incorrect number of values!");
                ((SchedulerTask) ccTask).Schedule = "";
                ccTask.Status = TaskStatus.ERROR;
            } else if (!Util.validateSchedule(((SchedulerTask) ccTask).Schedule)) {
                CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " schedule has incorrect format!");
                ((SchedulerTask) ccTask).Schedule = "";
                ccTask.Status = TaskStatus.ERROR;
            }
        } else if (ccTask instanceof IntervalTask) {
            ((IntervalTask) ccTask).Interval = rootNode.getNode("tasks", ccTask.Name, "interval").getInt(-1);
            if (((IntervalTask) ccTask).Interval < 1) {
                CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " interval is less than 1!");
                ccTask.Status = TaskStatus.ERROR;
            }
        }
        ccTask.Command = rootNode.getNode("tasks", ccTask.Name, "command").getString("");
        if (ccTask.Command.isEmpty()) {
            CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " command is not set!");
            ccTask.Status = TaskStatus.ERROR;
        } else if (Util.checkBlacklist(ccTask.Command)) {
            CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " command is blacklisted!");
            ccTask.Command = "";
            ccTask.Status = TaskStatus.ERROR;
        }
        ccTask.Description = rootNode.getNode("tasks", ccTask.Name, "description").getString("");
        if (ccTask.Description.isEmpty()) {
            CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " description is not set!");
        }
        if (ccTask.Status.equals(TaskStatus.ERROR)) {
            CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " status is locked to Error!");
        }
    }

    public static void writeConfig() {
        for (CmdCalTask ccTask : Tasks.taskMap.values()) {
            writeTask(ccTask);
        }
    }

    public static void writeTask(CmdCalTask ccTask) {
        loadConfig();
        Util.refreshStatus(ccTask);
        rootNode.getNode("tasks", ccTask.Name, "type").setValue(Util.printType(ccTask.Type));
        if (ccTask instanceof SchedulerTask) {
            rootNode.getNode("tasks", ccTask.Name, "schedule").setValue(((SchedulerTask) ccTask).Schedule);
        } else if (ccTask instanceof IntervalTask) {
            rootNode.getNode("tasks", ccTask.Name, "interval").setValue(((IntervalTask) ccTask).Interval);
        } else {
            CmdCalendar.getPlugin().getLogger().error(ccTask.Name + " TaskType not recognized!");
        }
        if (ccTask.Status.equals(TaskStatus.ERROR)) {
            CmdCalendar.getPlugin().getLogger().warn("Task " + ccTask.Name + " status is locked to Error!");
        }
        rootNode.getNode("tasks", ccTask.Name, "command").setValue(ccTask.Command);
        rootNode.getNode("tasks", ccTask.Name, "description").setValue(ccTask.Description);
        rootNode.getNode("tasks", ccTask.Name, "status").setValue(Util.printStatus(ccTask.Status));
        saveConfig();
    }

    public static void deleteTask(CmdCalTask ccTask) {
        loadConfig();
        rootNode.getNode("tasks", ccTask.Name).setValue(null);
        saveConfig();
    }

    private static void loadConfig() {
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            CmdCalendar.getPlugin().getLogger().error("Config could not load!");
        }
    }

    private static void saveConfig() {
        try {
            loader.save(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
            CmdCalendar.getPlugin().getLogger().error("Config could not save!");
        }
    }

    public static List<String> getBlacklist() {
        loadConfig();
        List<String> blacklist = Lists.newArrayList();
        try {
            blacklist.addAll(rootNode.getNode("config", "command_blacklist").getList(TypeToken.of(String.class)));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
            CmdCalendar.getPlugin().getLogger().error("Unable to read blacklist! Using defaults.");
            blacklist.addAll(Arrays.asList("start", "restart", "stop", "op", "cc", "cmdcal", "cmdcalendar"));
        }
        CmdCalendar.getPlugin().getLogger().info(String.join(", ", blacklist));
        return blacklist;
    }

    public static boolean isBlacklistCheck() {
        loadConfig();
        return rootNode.getNode("config", "command_blacklist_check").getBoolean(true);
    }

    public static boolean isActivateOnStartup() {
        loadConfig();
        return rootNode.getNode("config", "activate_on_startup").getBoolean(true);
    }

    public static boolean isIntervalDelayStart() {
        loadConfig();
        return rootNode.getNode("config", "interval_delay_start").getBoolean(true);
    }
}