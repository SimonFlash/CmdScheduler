package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcalendar.commands.*;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.TreeMap;

@Plugin(id = "cmdcalendar", name = "CmdCalendar", version = "mc1.10.2-v1.1.2", description = "Automatic Command Scheduler - Developed by Simon_Flash")
public class CmdCalendar {
    private static CmdCalendar plugin;
    public static CmdCalendar getPlugin() {
        return plugin;
    }

    private static URL wiki;
    public static URL getWiki() {
        return wiki;
    }

    private static URL discord;
    public static URL getDiscord() {
        return discord;
    }

    @Inject
    private Logger logger;
    public Logger getLogger() {
        return logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;
    public Path getDefaultConfig() {
        return defaultConfig;
    }

    @Listener
    public void onInitilization(GameInitializationEvent event) {
        plugin = this;
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|     CmdCalendar - Version 1.1.2     |");
        getLogger().info("|      Developed By: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Config.readConfig();
        try {
            wiki = new URL("https://github.com/SimonFlash/CmdCalendar/wiki");
        } catch (MalformedURLException ignored) {
            getLogger().error("Unable to locate CmdCalendar Wiki!");
        }
        try {
            discord = new URL("https://discordapp.com/invite/4wayq37");
        } catch (MalformedURLException ignored) {
            getLogger().error("Unable to locate CmdCalendar Discord!");
        }

        TreeMap<String, String> debugMap = new TreeMap<>();
        debugMap.put("ListConcealed", "listconcealed"); debugMap.put("listconcealed", "listconcealed");
        debugMap.put("LoadConfig", "loadconfig"); debugMap.put("loadconfig", "loadconfig");
        debugMap.put("ReloadTasks", "reloadtasks"); debugMap.put("reloadtasks", "reloadtasks");
        debugMap.put("SaveConfig", "saveconfig"); debugMap.put("saveconfig", "saveconfig");
        debugMap.put("StopAll", "stopall"); debugMap.put("stopall", "stopall");
        debugMap.put("Unconceal", "unconceal"); debugMap.put("unconceal", "unconceal");

        TreeMap<String, String> editTaskMap = new TreeMap<>();
        editTaskMap.put("Command", "command"); editTaskMap.put("Cmd", "command"); editTaskMap.put("c", "command");
        editTaskMap.put("Description", "description"); editTaskMap.put("Desc", "description"); editTaskMap.put("d", "description");
        editTaskMap.put("Interval", "interval"); editTaskMap.put("Int", "interval"); editTaskMap.put("i", "interval");
        editTaskMap.put("Name", "name"); editTaskMap.put("n", "name");
        editTaskMap.put("Schedule", "schedule"); editTaskMap.put("Sched", "schedule"); editTaskMap.put("s", "schedule");

        CommandSpec CreateTask = CommandSpec.builder()
                .executor(new CreateTask())
                .description(Text.of("Adds a task to the task list"))
                .permission("cmdcalendar.tasks.create")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("type"))))
                .build();
        CommandSpec Debug = CommandSpec.builder()
                .executor(new Debug())
                .description(Text.of("Accesses CmdCalendar debug commands"))
                .permission("cmdcalendar.debug")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("subcommand"), debugMap)),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("confirm"))))
                .build();
        CommandSpec DeleteTask = CommandSpec.builder()
                .executor(new DeleteTask())
                .description(Text.of("Removes a task from the task list"))
                .permission("cmdcalendar.tasks.delete")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("confirm"))))
                .build();
        CommandSpec ShowTask = CommandSpec.builder()
                .executor(new ShowTask())
                .description(Text.of("Shows detailed information about a task"))
                .permission("cmdcalendar.view.tasks")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .build();
        CommandSpec StartTask = CommandSpec.builder()
                .executor(new StartTask())
                .description(Text.of("Starts a task"))
                .permission("cmdcalendar.run.start")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .build();
        CommandSpec StopTask = CommandSpec.builder()
                .executor(new StopTask())
                .description(Text.of("Stops a task"))
                .permission("cmdcalendar.run.stop")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .build();
        CommandSpec TaskList = CommandSpec.builder()
                .executor(new TaskList())
                .description(Text.of("Shows basic info for all tasks"))
                .permission("cmdcalendar.view.list")
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("status"))))
                .build();
        CommandSpec EditTask = CommandSpec.builder()
                .executor(new EditTask())
                .description(Text.of("Changes the variable of a task"))
                .permission("cmdcalendar.tasks.edit")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("variable"), editTaskMap)),
                        GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("value"))))
                .build();
        CommandSpec CmdCalendar = CommandSpec.builder()
                .executor(new Base())
                .description(Text.of("Opens command reference menu (Use over /help CmdCalendar!)"))
                .permission("cmdcalendar.base")
                .child(CreateTask, "CreateTask", "Create", "ct")
                .child(Debug, "Debug")
                .child(DeleteTask, "DeleteTask", "Delete", "dt")
                .child(EditTask, "EditTask", "Edit", "et")
                .child(ShowTask, "ShowTask", "Show", "st")
                .child(StartTask, "StartTask", "Start", "on")
                .child(StopTask, "StopTask", "Stop", "off")
                .child(TaskList, "TaskList", "Tasks", "tl")
                .build();
        Sponge.getCommandManager().register(this, CmdCalendar, Lists.newArrayList("cc", "CmdCal", "CmdCalendar"));
    }

    @Listener
    public void onStartedServer(GameStartedServerEvent event) {
        Config.onStartedServer();
        if (Config.isActivateOnStartup()) {
            Tasks.reloadTasks();
        }
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.onReload();
        Config.readConfig();
        if (Config.isActivateOnStartup()) {
            Tasks.reloadTasks();
        }
    }

    @Listener
    public void onStoppingServer(GameStoppingServerEvent event) {
        Config.onStoppingServer();
        Config.writeConfig();
    }
}