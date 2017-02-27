package com.mcsimonflash.sponge.cmdcalendar.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class CmdCal implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Text.of(TextColors.DARK_AQUA, "+=-=-=-=-=[CmdCalendar]=-=-=-=-=+"));
        src.sendMessage(Text.builder("/CmdCal ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "CmdCal: ", TextColors.AQUA, "Opens command reference menu\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "CmdCal, CommandCalendar, CmdCalendar, CC\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.use")))
                .append(Text.builder("<Subcommand>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommands: ", TextColors.AQUA, "CreateTask, DeleteTask, EditTask, ShowTask, StartTask, StopTask, TaskList, ToggleTask\n")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal SyncConfig ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal SyncConfig "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "Config: ", TextColors.AQUA, "Used to save or load config changes\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "No Aliases exist.\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.syncconfig")))
                .append(Text.builder("<Subcommand> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommands: ", TextColors.AQUA, "Save, Load")))
                        .build())
                .append(Text.builder("Confirm")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Confirm: ", TextColors.AQUA, "Confirms changing the config.")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal CreateTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal CreateTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "CreateTask: ", TextColors.AQUA, "Adds a task to the task list\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "CreateTask, AddTask, Create, Add, ct, at\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.tasks.create")))
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .append(Text.builder("<Interval> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Interval<Integer>: ", TextColors.AQUA, "Time before repeating task [minutes].")))
                        .build())
                .append(Text.builder("<Command> >")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Command<String (joined)>: ", TextColors.AQUA, "Command run by the task.")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal DeleteTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal DeleteTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "DeleteTask: ", TextColors.AQUA, "Deletes a task from the task list\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "DeleteTask, RemoveTask, Delete, Remove, dt, rt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.tasks.delete")))
                .append(Text.builder("<Name>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal EditTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal EditTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "EditTask: ", TextColors.AQUA, "Changes the variable of a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "DeleteTask, RemoveTask, DelTask, Delete, Remove, Del, dt, rt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.tasks.edit")))
                .append(Text.builder("<Variable> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Parameter: ", TextColors.AQUA, "Parameter to be changed [Command, Description, Interval, Name]\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "SetCommand, Command, SetCmd, Cmd, sc, SetDescription, Description, SetDesc, Desc, sd, SetInterval, Interval, SetInt, Int, si, SetName, Name, sn\n",
                                TextColors.DARK_AQUA, "Permissions: ", TextColors.AQUA, "cmdcalendar.edit.cmd, cmdcalendar.edit.desc, cmdcalendar.edit.int, cmdcaledar.edit.name")))
                        .build())
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .append(Text.builder("<Value> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Value: ", TextColors.AQUA, "Value to be changed [Matches type of Parameter]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal ShowTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal ShowTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "ShowTask: ", TextColors.AQUA, "Shows detailed information about a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "ShowTask, Show, st\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.view.task")))
                .append(Text.builder("<Name>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal StartTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal StartTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "StartTask: ", TextColors.AQUA, "Starts a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "StartTask, Start, on, t+\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.run.start")))
                .append(Text.builder("<Name>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal StopTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal StopTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "StopTask: ", TextColors.AQUA, "Stops a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "ShowTask, Show, off, t-\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.run.stop")))
                .append(Text.builder("<Name>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal TaskList ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal TaskList "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "TaskList: ", TextColors.AQUA, "Shows basic information about all tasks\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "TaskList, ListTasks, Tasks, List, tl, lt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.view.list")))
                .build());
        src.sendMessage(Text.builder("/CmdCal ToggleTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal ToggleTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "ToggleTask: ", TextColors.AQUA, "Changes the run state of a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "ToggleTask, Toggle, tt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.run.toggle")))
                .append(Text.builder("<Name>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces].")))
                        .build())
                .build());

        return CommandResult.success();
    }
}