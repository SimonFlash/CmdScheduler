package com.mcsimonflash.sponge.cmdscheduler.internal;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.configuration.ConfigHolder;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.configuration.NodeUtils;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.schedule.*;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.util.Tristate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    private static Path directory = CmdScheduler.get().getDirectory(), configuration = directory.resolve("configuration");
    private static ConfigHolder<CommentedConfigurationNode> config;

    //TODO: Edit tasks in-game
    public static Map<String, CommandTask> tasks = Maps.newHashMap();

    public static void load() {
        tasks.values().forEach(t -> t.getTask().stop(CmdScheduler.get().getContainer()));
        tasks.clear();
        try {
            Files.createDirectories(configuration);
            if (Files.notExists(configuration.resolve("tasks.conf"))) {
                CmdScheduler.get().getContainer().getAsset("tasks.conf").get().copyToDirectory(configuration);
            }
            config = ConfigHolder.of(HoconConfigurationLoader.builder().setPath(configuration.resolve("tasks.conf")).build());
            config.getNode().getChildrenMap().values().forEach(Config::loadTask);
        } catch (IOException e) {
            CmdScheduler.get().getLogger().error("An unexpected error occurred loading the config: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadTask(ConfigurationNode node) {
        try {
            String name = (String) node.getKey();
            String command = node.getNode("command").getString("");
            Schedule schedule = getSchedule(node.getNode("schedule"));
            Tristate async = node.getNode("async").isVirtual() ? Tristate.UNDEFINED : Tristate.fromBoolean(node.getNode("async").getBoolean(false));
            tasks.put(name.toLowerCase(), new CommandTask(name, command.startsWith("/") ? command.substring(1) : command, schedule, async));
        } catch (IllegalArgumentException e) {
            CmdScheduler.get().getLogger().error(e.getMessage());
        }
    }

    private static Schedule getSchedule(ConfigurationNode node) throws IllegalArgumentException {
        switch (node.getNode("type").getString("").toLowerCase()) {
            case "calendar":
                return CalendarSchedule.builder()
                        .date(node.getNode("date").getString(""))
                        .interval(Utils.parseTime(node.getNode("interval").getString("")))
                        .build();
            case "classic":
                ClassicSchedule.Builder classic = ClassicSchedule.builder();
                NodeUtils.ifAttached(node.getNode("delay"), n -> classic.delay(Utils.parseTime(n.getString("0"))));
                NodeUtils.ifAttached(node.getNode("delay-ticks"), n -> classic.delayTicks(n.getLong(0)));
                NodeUtils.ifAttached(node.getNode("interval"), n -> classic.interval(Utils.parseTime(n.getString(""))));
                NodeUtils.ifAttached(node.getNode("interval-ticks"), n -> n.getLong(0));
                return classic.build();
            case "cron":
                CronSchedule.Builder cron = CronSchedule.builder();
                if (!node.getNode("schedule").isVirtual()) {
                    cron.schedule(node.getNode("schedule").getString(""));
                } else {
                    for (Units unit : Units.values()) {
                        NodeUtils.ifAttached(node.getNode(unit.name().toLowerCase()), n -> n.getString(""));
                    }
                }
                return cron.build();
            default:
                throw new IllegalArgumentException("Unknown schedule type " + node.getNode("type").getString("undefined"));
        }
    }

}