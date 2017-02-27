package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Plugin(id = "cmdcalendar", name = "CmdCalendar", version = "1.0.1-ALPHA", description = "Command Scheduler")
public class CmdCalendar {
    private static CmdCalendar plugin;
    static CmdCalendar getPlugin() {
        return plugin;
    }

    @Inject
    private Logger logger;
    Logger getLogger() {
        return logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;
    Path getDefaultConfig() {
        return defaultConfig;
    }

    @Listener
    public void onInitilization(GameInitializationEvent event) {
        plugin = this;
        CmdCalendar.getPlugin().getLogger().info("Initializing task resources...");

        Map<String, String> map_Config = new HashMap<>();
        map_Config.put("Save", "taskSave");
        map_Config.put("Load", "taskLoad");
        CommandSpec cmdSpec_Config = CommandSpec.builder()
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("parameter"), map_Config)),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .executor(new Command_Config())
                .permission("cmdcalendar.config")
                .build();

        CommandSpec cmdSpec_CreateTask = CommandSpec.builder()
                .description(Text.of("Adds a task to the task list"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("taskInterval"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskCommand")))
                .executor(new Command_CreateTask())
                .permission("cmdcalendar.task.create")
                .build();

        CommandSpec cmdSpec_DeleteTask = CommandSpec.builder()
                .description(Text.of("Removes a task from the task list"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new Command_DeleteTask())
                .permission("cmdcalendar.task.delete")
                .build();

        CommandSpec cmdSpec_SetCommand = CommandSpec.builder()
                .description(Text.of("Changes the command of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskCommand")))
                .executor(new Command_SetCommand())
                .permission("cmdcalendar.task.edit.cmd")
                .build();

        CommandSpec cmdSpec_SetDescription = CommandSpec.builder()
                .description(Text.of("Changes the description of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.remainingJoinedStrings(Text.of("taskDescription")))
                .executor(new Command_SetDescription())
                .permission("cmdcalendar.task.edit.desc")
                .build();

        CommandSpec cmdSpec_SetInterval = CommandSpec.builder()
                .description(Text.of("Changes the description of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("taskInterval"))))
                .executor(new Command_SetInterval())
                .permission("cmdcalendar.task.edit.int")
                .build();

        CommandSpec cmdSpec_SetName = CommandSpec.builder()
                .description(Text.of("Changes the name of a task (uses EditTask)"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("newTaskName"))))
                .executor(new Command_SetName())
                .permission("cmdcalendar.task.edit.name")
                .build();

        CommandSpec cmdSpec_ShowTask = CommandSpec.builder()
                .description(Text.of("Shows detailed information about a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new Command_ShowTask())
                .permission("cmdcalendar.view.task")
                .build();

        CommandSpec cmdSpec_StartTask = CommandSpec.builder()
                .description(Text.of("Starts a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new Command_StartTask())
                .permission("cmdcalendar.run.start")
                .build();

        CommandSpec cmdSpec_StopTask = CommandSpec.builder()
                .description(Text.of("Stops a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new Command_StopTask())
                .permission("cmdcalendar.run.stop")
                .build();

        CommandSpec cmdSpec_ToggleTask = CommandSpec.builder()
                .description(Text.of("Changes the run state of a task"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .executor(new Command_ToggleTask())
                .permission("cmdcalendar.run.toggle")
                .build();

        CommandSpec cmdSpec_TaskList = CommandSpec.builder()
                .description(Text.of("Shows basic info for all task"))
                .executor(new Command_TaskList())
                .permission("cmdcalendar.view.all")
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
                .executor(new Command_EditTask())
                .build();

        CommandSpec cmdSpec_CmdCalendar = CommandSpec.builder()
                .description(Text.of("Opens command reference menu (Use over /help CmdCalendar!)"))
                .child(cmdSpec_CreateTask, "CreateTask", "AddTask", "Create", "Add", "ct", "at")
                .child(cmdSpec_DeleteTask, "DeleteTask", "RemoveTask", "DelTask", "Delete", "Remove", "Del", "dt", "rt")
                .child(cmdSpec_EditTask, "EditTask", "Edit", "et")
                .child(cmdSpec_ShowTask, "ShowTask", "Show", "st")
                .child(cmdSpec_StartTask, "StartTask", "ActivateTask", "Start", "Activate", "on", "t+")
                .child(cmdSpec_StopTask, "StopTask", "HaltTask", "Stop", "Halt", "off", "t-")
                .child(cmdSpec_TaskList, "TaskList", "Tasks", "ListTask", "List", "tl", "lt")
                .child(cmdSpec_ToggleTask, "ToggleTask", "Toggle", "tt")
                .child(cmdSpec_Config, "Config", "conf", "cfg")
                .permission("cmdcalendar.use")
                .executor(new Command_CmdCal())
                .build();
        Sponge.getCommandManager().register(this, cmdSpec_CmdCalendar, Lists.newArrayList("cc", "CmdCal", "CmdCalendar", "CommandCalendar"));

        CmdCalendar.getPlugin().getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        CmdCalendar.getPlugin().getLogger().info("| CmdCalendar - Simon_Flash |");
        CmdCalendar.getPlugin().getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=+");

        Manager_Config.readConfig();
        Handler_RunTask.syncTasks();
        for (Object_CmdCalTask task : Manager_Tasks.getTaskList())
        {
            CmdCalendar.getPlugin().getLogger().info("" + task.getTaskStatus());
        }
    }
}