package com.mcsimonflash.sponge.cmdcalendar.managers;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Config {
    private static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
            .setPath(CmdCalendar.getPlugin().getDefaultConfig())
            .build();
    private static CommentedConfigurationNode rootNode;

    private static List<String> blacklist = Lists.newArrayList();
    private static boolean blacklistCheck;
    private static boolean activateOnStartup;
    private static boolean intervalDelayStart;
    private static TimeUnit intervalTimeUnit;

    public static void readConfig() {
        if (Files.notExists(CmdCalendar.getPlugin().getDefaultConfig())) {
            try {
                Sponge.getAssetManager().getAsset(CmdCalendar.getPlugin(), "defaultConfig.conf").get().copyToFile(CmdCalendar.getPlugin().getDefaultConfig());
                CmdCalendar.getPlugin().getLogger().warn("Default Config loaded! Edit cmdcalendar.conf to change settings and create tasks!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();

        Tasks.removeAll();
        RunTask.removeAll();

        blacklistCheck = rootNode.getNode("config", "task_blacklist_check").getBoolean(true);
        activateOnStartup = rootNode.getNode("config", "activate_on_startup").getBoolean(true);
        intervalDelayStart = rootNode.getNode("config", "interval_delay_start").getBoolean(true);
        intervalTimeUnit = parseTimeUnit(rootNode.getNode("config", "interval_time_unit").getString("Seconds"));

        try {
            rootNode.getNode("config", "task_blackslist").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
            CmdCalendar.getPlugin().getLogger().error("[ERROR]: unable to read blacklist! Reverting to defaults.");
            blacklist.addAll(Arrays.asList(("start, restart, stop, cc, cmdcal, cmdcalendar, commandcalendar").split(", ")));
        }

        blacklist.addAll(Arrays.asList(rootNode.getNode("config", "blacklist").getString("start, restart, stop, cc, cmdcal, cmdcalendar, commandcalendar").split(", ")));

        List<String> loadedTasks = Lists.newArrayList();
        Map<Object, ? extends ConfigurationNode> taskNameMap = rootNode.getNode("tasks").getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> task : taskNameMap.entrySet()) {
            String taskName = (String) task.getKey();
            TaskType taskType = CmdCalTask.parseType(rootNode.getNode("tasks", taskName, "type").getString(""));

            if (Tasks.verifyTask(taskName)) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " name has multiple instances!");
                break;
            }
            if (taskType.equals(TaskType.ERROR)) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " type is not valid!");
                break;
            }

            CmdCalendar.getPlugin().getLogger().debug("[DEBUG]: Adding task " + taskName);
            CmdCalTask ccTask = Tasks.newTask(taskName, taskType);
            loadedTasks.add(taskName);
            CmdCalendar.getPlugin().getLogger().debug("[DEBUG]: Added " + taskName);

            if (ccTask instanceof Scheduler) {

                String taskSchedule = rootNode.getNode("tasks", taskName, "schedule").getString("");
                String[] split = taskSchedule.split(" ");
                if (split.length != 5) {
                    CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " schedule has incorrect number of values!");
                }
                ((Scheduler) ccTask).setSchedule(taskSchedule);
                for (String str : split) {
                    if (str.equals("*")) {
                        break;
                    } else if (str.length() > 1 && str.substring(0,1).equals("/")) {
                        str = str.substring(1);
                    }
                    try {
                        Integer.parseInt(str);
                    } catch (NumberFormatException ignored) {
                        CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " schedule has incorrect format!");
                        ((Scheduler) ccTask).setSchedule("");
                    }
                }

            } else if (ccTask instanceof Interval) {
                int taskInterval = rootNode.getNode("tasks", taskName, "interval").getInt(-1);
                if (taskInterval < 1) {
                    CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " interval is less than 1!");
                } else {
                    ((Interval) Tasks.getTask(taskName)).setInterval(taskInterval);
                }
            }

            TaskStatus taskStatus = CmdCalTask.parseStatus(rootNode.getNode("tasks", taskName, "status").getString("Halted"));
            if (activateOnStartup && taskStatus != TaskStatus.ERROR) {
                ccTask.setStatus(TaskStatus.Halted);
            } else {
                ccTask.setStatus(taskStatus);
            }

            String taskCommand = rootNode.getNode("tasks", taskName, "command").getString("");
            if (taskCommand.isEmpty()) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " command is not set!");
                ccTask.setStatus(TaskStatus.ERROR);
            } else if (blacklistCheck && Commands.testCommandBlacklisted(taskCommand)) {
                CmdCalendar.getPlugin().getLogger().error("[ERROR]: " + taskName + " command is blacklisted!");
                ccTask.setStatus(TaskStatus.ERROR);
            } else {
                ccTask.setCommand(taskCommand);
            }

            String taskDescription = rootNode.getNode("tasks", taskName, "description").getString("");
            if (taskDescription.isEmpty()) {
                CmdCalendar.getPlugin().getLogger().warn("[WARN]: " + taskName + " description is not set!");
            } else {
                ccTask.setDescription(taskDescription);
            }
        }

        if (loadedTasks.isEmpty()) {
            CmdCalendar.getPlugin().getLogger().info("No tasks loaded!");
        } else {
            CmdCalendar.getPlugin().getLogger().info("Loaded " + String.join(", ", loadedTasks) + ".");
            for (String str : loadedTasks) {
                CmdCalendar.getPlugin().getLogger().info("[DEBUG]: Found " + Tasks.getTask(str) + " in task map");
            }
            RunTask.syncTasks();
        }
    }

    public static boolean writeConfig() {
        loadConfig();

        rootNode.getNode("config", "task_blacklist_check").setValue(blacklistCheck);
        rootNode.getNode("config", "task_activate_on_startup").setValue(activateOnStartup);
        rootNode.getNode("config", "interval_delay_start").setValue(intervalDelayStart);
        rootNode.getNode("config", "interval_time_unit").setValue(printTimeUnit(intervalTimeUnit));

        rootNode.getNode("config", "task_blacklist").setValue(blacklist);

        for (CmdCalTask ccTask : Tasks.getSortedTaskMap().values()) {
            syncTask(ccTask);
        }

        if (saveConfig()) {
            CmdCalendar.getPlugin().getLogger().info("[CmdCal]: Config successfully saved.");
            return true;
        } else {
            CmdCalendar.getPlugin().getLogger().error("[ERROR]: Config could not save!");
            return false;
        }
    }

    public static void syncTask(CmdCalTask ccTask) {
        Tasks.reloadStatus(ccTask.getName());
        rootNode.getNode("tasks", ccTask.getName(), "type").setValue(CmdCalTask.printType(ccTask.getType()));
        if (ccTask instanceof Scheduler) {
            rootNode.getNode("tasks", ccTask.getName(), "schedule").setValue(((Scheduler) ccTask).getSchedule());
        } else if (ccTask instanceof Interval) {
            rootNode.getNode("tasks", ccTask.getName(), "interval").setValue(((Interval) ccTask).getInterval());
        } else {
            CmdCalendar.getPlugin().getLogger().info("[ERROR]: " + ccTask.getName() + " TaskType not recognized!");
        }
        rootNode.getNode("tasks", ccTask.getName(), "command").setValue(ccTask.getCommand());
        rootNode.getNode("tasks", ccTask.getName(), "description").setValue(ccTask.getDescription());
        rootNode.getNode("tasks", ccTask.getName(), "status").setValue(CmdCalTask.printStatus(ccTask.getStatus()));
    }

    public static void deleteTask(CmdCalTask ccTask) {
        rootNode.getNode("tasks", ccTask.getName()).setValue(null);
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

    public static TimeUnit parseTimeUnit(String time) {
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
                CmdCalendar.getPlugin().getLogger().warn("Interval Time Unit not found - currently using Seconds");
                return TimeUnit.SECONDS;
        }
    }

    public static String printTimeUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS:
                return "Seconds";
            case MINUTES:
                return "Minutes";
            case HOURS:
                return "Hours";
            case DAYS:
                return "Days";
            default:
                CmdCalendar.getPlugin().getLogger().warn("Interval Time Unit not found - reverting to Seconds");
                return "Seconds";
        }
    }

    public static List<String> getBlacklist() {
        return blacklist;
    }
    public static boolean isBlacklistCheck() {
        return blacklistCheck;
    }
    public static boolean isActivateOnStartup() {
        return activateOnStartup;
    }
    public static boolean isIntervalDelayStart() {
        return intervalDelayStart;
    }
    public static TimeUnit getIntervalTimeUnit() {
        return intervalTimeUnit;
    }
}