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
        src.sendMessage(Text.builder("/CmdCal CreateTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal CreateTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "CreateTask: ", TextColors.AQUA, "Adds a task to the task list\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "CreateTask, AddTask, Create, Add, ct, at\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.tasks.create")))
                .append(Text.builder("<Type> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Type: ", TextColors.AQUA, "Type of Task to be created [Scheduler, Interval]")))
                        .build())
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal EditTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal EditTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "EditTask: ", TextColors.AQUA, "Changes the specified variable of a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "EditTask, Edit, et\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.tasks.edit")))
                .append(Text.builder("<Variable> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Parameter: ", TextColors.AQUA, "Parameter to be changed [Command, Description, Interval, Name, Schedule]\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "SetCommand, Command, SetCmd, Cmd, sc, SetDescription, Description, SetDesc, Desc, sd, SetInterval, Interval, SetInt, Int, si, SetName, Name, sn, SetSchedule, Schedule, SetSched, Sched, ss\n",
                                TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "SetInterval and SetSchedule may only be used on Interval and Scheduler tasks respectively\n",
                                TextColors.DARK_AQUA, "Permissions: ", TextColors.AQUA, "cmdcalendar.edit.command, cmdcalendar.edit.description, cmdcalendar.edit.interval, cmdcaledar.edit.name, cmdcalendar.edit.schedule")))
                        .build())
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task [no spaces]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal TaskList ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal TaskList "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "TaskList: ", TextColors.AQUA, "Shows basic information about all tasks\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "TaskList, ListTasks, Tasks, List, tl, lt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.view.list")))
                .append(Text.builder("<Status>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Status: ", TextColors.AQUA, "Optional status modifier [Active, Halted, Suspended, Concealed]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal Debug ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal Debug "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "TaskList: ", TextColors.AQUA, "Accesses CmdCalendar debug commands\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "Debug, db\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.debug")))
                .append(Text.builder("<Subcommand>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommand: ", TextColors.AQUA, "Subcommand to be called [ListConcealed, LoadConfig, SaveConfig, StopAll]\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "Debug commands do not have aliases.\n",
                                TextColors.DARK_AQUA, "Permissions: ", TextColors.AQUA, "cmdcalendar.debug.listconcealed, cmdcalendar.debug.loadconfig, cmdcalendar.debug.saveconfig, cmdcaledar.debug.stopall")))
                        .build())
                .append(Text.builder("Confirm")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Confirm: ", TextColors.AQUA, "Confirms sending a debug command")))
                        .build())
                .build());

        return CommandResult.success();
    }
}