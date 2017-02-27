package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.mcsimonflash.sponge.cmdcalendar.commands.*;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Plugin(id = "cmdcalendar", name = "CmdCalendar", version = "1.1.1-ALPHA", description = "Automatic Command Scheduler [WIP Alpha] - Developed by Simon_Flash")
public class CmdCalendar {
    private static CmdCalendar plugin;
    public static CmdCalendar getPlugin() {
        return plugin;
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
        CmdCalendar.getPlugin().getLogger().info("Initializing task resources...");

        CommandSpec cmdSpec_CreateTask = CommandSpec.builder()
                .description(Text.of("Adds a task to the task list"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("taskInterval"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskCommand")))
                .executor(new CreateTask())
                .permission("cmdcalendar.tasks.create")
                .build();

        CommandSpec cmdSpec_DeleteTask = CommandSpec.builder()
                .description(Text.of("Removes a task from the task list"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new DeleteTask())
                .permission("cmdcalendar.tasks.delete")
                .build();

        CommandSpec cmdSpec_SetCommand = CommandSpec.builder()
                .description(Text.of("Changes the command of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskCommand")))
                .executor(new SetCommand())
                .permission("cmdcalendar.tasks.edit.cmd")
                .build();

        CommandSpec cmdSpec_SetDescription = CommandSpec.builder()
                .description(Text.of("Changes the description of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskDescription")))
                .executor(new SetDescription())
                .permission("cmdcalendar.tasks.edit.desc")
                .build();

        CommandSpec cmdSpec_SetInterval = CommandSpec.builder()
                .description(Text.of("Changes the description of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("taskInterval"))))
                .executor(new SetInterval())
                .permission("cmdcalendar.tasks.edit.int")
                .build();

        CommandSpec cmdSpec_SetName = CommandSpec.builder()
                .description(Text.of("Changes the name of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("newTaskName"))))
                .executor(new SetName())
                .permission("cmdcalendar.tasks.edit.name")
                .build();

        CommandSpec cmdSpec_ShowTask = CommandSpec.builder()
                .description(Text.of("Shows detailed information about a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new ShowTask())
                .permission("cmdcalendar.view.tasks")
                .build();

        CommandSpec cmdSpec_StartTask = CommandSpec.builder()
                .description(Text.of("Starts a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new StartTask())
                .permission("cmdcalendar.run.start")
                .build();

        CommandSpec cmdSpec_StopTask = CommandSpec.builder()
                .description(Text.of("Stops a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new StopTask())
                .permission("cmdcalendar.run.stop")
                .build();

        CommandSpec cmdSpec_SyncConfig = CommandSpec.builder()
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("parameter"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .executor(new SyncConfig())
                .permission("cmdcalendar.syncconf")
                .build();

        CommandSpec cmdSpec_ToggleTask = CommandSpec.builder()
                .description(Text.of("Changes the run state of a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new ToggleTask())
                .permission("cmdcalendar.run.toggle")
                .build();

        CommandSpec cmdSpec_TaskList = CommandSpec.builder()
                .description(Text.of("Shows basic info for all task"))
                .executor(new TaskList())
                .permission("cmdcalendar.view.list")
                .build();

        Map<String, String> map_EditTask = new HashMap<>();
        map_EditTask.put("Name", "taskName");
        map_EditTask.put("Interval", "taskInterval");
        map_EditTask.put("Description", "taskDescription");
        map_EditTask.put("Command", "taskCommand");
        CommandSpec cmdSpec_EditTask = CommandSpec.builder()
                .description(Text.of("Changes the variable of a task"))
                .child(cmdSpec_SetCommand, "SetCommand", "Command", "SetCmd", "Cmd", "sc")
                .child(cmdSpec_SetDescription, "SetDescription", "Description", "SetDesc", "Desc", "sd")
                .child(cmdSpec_SetInterval, "SetInterval", "Interval", "SetInt", "Int", "si")
                .child(cmdSpec_SetName, "SetName", "Name", "sn")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("parameter"), map_EditTask)),
                        GenericArguments.remainingJoinedStrings(Text.of("value")))
                .permission("cmdcalendar.edit")
                .executor(new EditTask())
                .build();

        CommandSpec cmdSpec_CmdCalendar = CommandSpec.builder()
                .description(Text.of("Opens command reference menu (Use over /help CmdCalendar!)"))
                .child(cmdSpec_SyncConfig, "SyncConfig")
                .child(cmdSpec_CreateTask, "CreateTask", "AddTask", "Create", "Add", "ct", "at")
                .child(cmdSpec_DeleteTask, "DeleteTask", "RemoveTask", "DelTask", "Delete", "Remove", "Del", "dt", "rt")
                .child(cmdSpec_EditTask, "EditTask", "Edit", "et")
                .child(cmdSpec_ShowTask, "ShowTask", "Show", "st")
                .child(cmdSpec_StartTask, "StartTask", "ActivateTask", "Start", "Activate", "on", "t+")
                .child(cmdSpec_StopTask, "StopTask", "HaltTask", "Stop", "Halt", "off", "t-")
                .child(cmdSpec_TaskList, "TaskList", "Tasks", "ListTask", "List", "tl", "lt")
                .child(cmdSpec_ToggleTask, "ToggleTask", "Toggle", "tt")
                .permission("cmdcalendar.use")
                .executor(new CmdCal())
                .build();
        Sponge.getCommandManager().register(this, cmdSpec_CmdCalendar, Lists.newArrayList("cc", "CmdCal", "CmdCalendar", "CommandCalendar"));

        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|  CmdCalendar - Version 1.1.1-ALPHA  |");
        getLogger().info("|                                     |");
        getLogger().info("|      Developed by: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");

        Config.readConfig();
        RunTask.setupTasks();

        getLogger().info("CmdCal: Initilization complete.");
    }
}