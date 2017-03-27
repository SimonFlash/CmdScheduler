package com.mcsimonflash.sponge.cmdcalendar;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcalendar.commands.*;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.TreeMap;

@Plugin(id = "cmdcalendar", name = "CmdCalendar", version = "mc1.10.2-1.0.0", description = "Automatic Command Scheduler - Developed by Simon_Flash")
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

        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|  CmdCalendar - Version 1.0.0-BETA   |");
        getLogger().info("|      Developed by: Simon_Flash      |");
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");

        CommandSpec cmdSpec_CreateTask = CommandSpec.builder()
                .executor(new CreateTask())
                .description(Text.of("Adds a task to the task list"))
                .permission("cmdcalendar.task.create")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskType"))))
                .build();

        CommandSpec cmdSpec_DeleteTask = CommandSpec.builder()
                .executor(new DeleteTask())
                .description(Text.of("Removes a task from the task list"))
                .permission("cmdcalendar.tasks.delete")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .build();

        CommandSpec cmdSpec_ListConcealed = CommandSpec.builder()
                .executor(new ListConcealed())
                .description(Text.of("Shows all concealed tasks"))
                .permission("cmdcalendar.debug.listconcealed")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .build();

        CommandSpec cmdSpec_LoadConfig = CommandSpec.builder()
                .executor(new LoadConfig())
                .description(Text.of("Loads tasks from the config"))
                .permission("cmdcalendar.debug.loadconfig")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .build();

        CommandSpec cmdSpec_SaveConfig = CommandSpec.builder()
                .executor(new SaveConfig())
                .description(Text.of("Saves tasks to the config"))
                .permission("cmdcalendar.debug.saveconfig")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .build();

        CommandSpec cmdSpec_ShowTask = CommandSpec.builder()
                .executor(new ShowTask())
                .description(Text.of("Shows detailed information about a task"))
                .permission("cmdcalendar.view.tasks")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .build();

        CommandSpec cmdSpec_StartTask = CommandSpec.builder()
                .executor(new StartTask())
                .description(Text.of("Starts a task"))
                .permission("cmdcalendar.run.start")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .build();

        CommandSpec cmdSpec_StopAll = CommandSpec.builder()
                .executor(new StopAll())
                .description(Text.of("Stops all tasks"))
                .permission("cmdcalendar.debug.stopall")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("cmdConfirm"))))
                .build();

        CommandSpec cmdSpec_StopTask = CommandSpec.builder()
                .executor(new StopTask())
                .description(Text.of("Stops a task"))
                .permission("cmdcalendar.run.stop")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))))
                .build();

        CommandSpec cmdSpec_TaskList = CommandSpec.builder()
                .executor(new TaskList())
                .description(Text.of("Shows basic info for all tasks"))
                .permission("cmdcalendar.view.list")
                .build();

        CommandSpec cmdSpec_Debug = CommandSpec.builder()
                .executor(new EditTask())
                .description(Text.of("Accesses CmdCalendar debug commands"))
                .permission("cmdcalendar.debug")
                .child(cmdSpec_ListConcealed, "ListConcealed")
                .child(cmdSpec_LoadConfig, "LoadConfig")
                .child(cmdSpec_SaveConfig, "SaveConfig")
                .child(cmdSpec_StopAll, "StopAll")
                .build();

        TreeMap<String, String> variableMap = new TreeMap<>();
        variableMap.put("command", "command"); variableMap.put("cmd", "command"); variableMap.put("c", "command");
        variableMap.put("description", "description"); variableMap.put("desc", "description"); variableMap.put("d", "description");
        variableMap.put("interval", "interval"); variableMap.put("inter", "interval"); variableMap.put("i", "interval");
        variableMap.put("name", "name"); variableMap.put("n", "name");
        variableMap.put("schedule", "schedule"); variableMap.put("sched", "schedule"); variableMap.put("s", "schedule");

        CommandSpec cmdSpec_EditTask = CommandSpec.builder()
                .executor(new EditTask())
                .description(Text.of("Changes the variable of a task"))
                .permission("cmdcalendar.edit")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("taskName"))),
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("variable"), variableMap)),
                        GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("value"))))
                .build();

        CommandSpec cmdSpec_CmdCalendar = CommandSpec.builder()
                .executor(new Help())
                .description(Text.of("Opens command reference menu (Use over /help CmdCalendar!)"))
                .permission("cmdcalendar.help")
                .child(cmdSpec_CreateTask, "CreateTask", "AddTask", "Create", "Add", "ct", "at")
                .child(cmdSpec_Debug, "Debug")
                .child(cmdSpec_DeleteTask, "DeleteTask", "RemoveTask", "Delete", "Remove", "dt", "rt")
                .child(cmdSpec_EditTask, "EditTask", "Edit", "et")
                .child(cmdSpec_ShowTask, "ShowTask", "Show", "st")
                .child(cmdSpec_StartTask, "StartTask", "ActivateTask", "Start", "Activate", "on", "t+")
                .child(cmdSpec_StopTask, "StopTask", "HaltTask", "Stop", "Halt", "off", "t-")
                .child(cmdSpec_TaskList, "TaskList", "Tasks", "ListTask", "List", "tl", "lt")
                .build();

        Sponge.getCommandManager().register(this, cmdSpec_CmdCalendar, Lists.newArrayList("cc", "CmdCal", "CmdCalendar", "CommandCalendar"));

        Config.readConfig();
    }

    public void onStopping(GameStoppingEvent event) {
        Config.writeConfig();
    }
}