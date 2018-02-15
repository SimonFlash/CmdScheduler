package com.mcsimonflash.sponge.cmdscheduler.internal;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import com.mcsimonflash.sponge.cmdscheduler.serializer.AdvancedTaskSerializer;
import com.mcsimonflash.sponge.cmdscheduler.serializer.ScheduleSerializer;
import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    private static Path directory = CmdScheduler.get().Directory, configuration = directory.resolve("configuration");
    private static ConfigurationOptions options = ConfigurationOptions.defaults();
    private static CommentedConfigurationNode root;

    //TODO: Edit tasks in-game
    public static Map<String, AdvancedTask> tasks = Maps.newHashMap();

    private static void initialize() {
        try {
            Files.createDirectories(configuration);
            if (Files.notExists(configuration.resolve("tasks.conf"))) {
                CmdScheduler.get().Container.getAsset("tasks.conf").get().copyToFile(configuration.resolve("tasks.conf"));
            }
            options = options.setSerializers(options.getSerializers().newChild()
                    .registerType(TypeToken.of(AdvancedTask.class), new AdvancedTaskSerializer())
                    .registerPredicate(TypeToken.of(Schedule.class)::isSupertypeOf, new ScheduleSerializer()));
            root = HoconConfigurationLoader.builder().setPath(configuration.resolve("tasks.conf")).build().load(options);
        } catch (IOException e) {
            CmdScheduler.get().Logger.error("An unexpected error occurred loading the config.", e);
        }
    }

    public static void load() {
        initialize();
        tasks.values().forEach(t -> t.stop(CmdScheduler.get().Container));
        tasks.clear();
        for (CommentedConfigurationNode node : root.getChildrenMap().values()) {
            try {
                AdvancedTask task = node.getValue(TypeToken.of(AdvancedTask.class));
                tasks.put(task.getName().toLowerCase(), task);
            } catch (ObjectMappingException e) {
                CmdScheduler.get().Logger.error("An error occurred deserializing a task from node " + node.getKey() + ".", e);
            }
        }
    }

}