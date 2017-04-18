package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.CmdCalendar;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Base implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Text.of(TextColors.DARK_AQUA, "+=-=-=-=-=[", TextColors.AQUA, "CmdCalendar", TextColors.DARK_AQUA, "]=-=-=-=-=+"));
        src.sendMessage(Text.builder("/CmdCal ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "CmdCal: ", TextColors.AQUA, "Opens command reference menu\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "CmdCal, CommandCalendar, CmdCalendar, cc\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.help")))
                .append(Text.builder("<Subcommand>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommands: ", TextColors.AQUA, "CreateTask, DeleteTask, EditTask, ShowTask, StartTask, StopTask, TaskList, Debug\n")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal CreateTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal CreateTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "CreateTask: ", TextColors.AQUA, "Adds a task to the task list\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "CreateTask, AddTask, Create, Add, ct, at\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.task.create")))
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
                        .build())
                .append(Text.builder("<Type> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Type: ", TextColors.AQUA, "Type of Task to be created [Scheduler, Interval]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal DeleteTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal DeleteTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "DeleteTask: ", TextColors.AQUA, "Deletes a task from the task list\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "DeleteTask, RemoveTask, Delete, Remove, dt, rt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.task.delete")))
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
                        .build())
                .append(Text.builder("<Confirm>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Confirm: ", TextColors.AQUA, "Confirms deleting the task")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal EditTask ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal EditTask "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "EditTask: ", TextColors.AQUA, "Changes the specified variable of a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "EditTask, Edit, et\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.task.edit")))
                .append(Text.builder("<Name> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
                        .build())
                .append(Text.builder("<Variable> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Variable: ", TextColors.AQUA, "Variable to be changed [Command, Description, Interval, Name, Schedule]\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "Command, cmd, c, Description, desc, d, Interval, inter, i, Name, n, Schedule, sched, s\n",
                                TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "Interval and Schedule may only be used on Interval and Scheduler tasks respectively\n")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
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
                                TextColors.DARK_AQUA, "Task Name<String>: ", TextColors.AQUA, "Name of the task")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal TaskList ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal TaskList "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "TaskList: ", TextColors.AQUA, "Shows basic information about all tasks\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "TaskList, ListTasks, Tasks, List, tl, lt\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.view.list")))
                .append(Text.builder("<Opt-Status>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Task Status: ", TextColors.AQUA, "Optional status modifier [Active, Halted, Error]")))
                        .build())
                .build());
        src.sendMessage(Text.builder("/CmdCal Debug ")
                .color(TextColors.DARK_AQUA)
                .onClick(TextActions.suggestCommand("/CmdCal Debug "))
                .onHover(TextActions.showText(Text.of(
                        TextColors.DARK_AQUA, "Debug: ", TextColors.AQUA, "Changes the specified variable of a task\n",
                        TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "EditTask, Edit, et\n",
                        TextColors.DARK_AQUA, "Permission: ", TextColors.AQUA, "cmdcalendar.task.edit")))
                .append(Text.builder("<Subcommand> ")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Subcommand: ", TextColors.AQUA, "Debug subcommand [ListConcealed, LoadConfig, ReloadTasks, SaveConfig, StopAll, UnConceal]\n",
                                TextColors.DARK_AQUA, "Aliases: ", TextColors.AQUA, "Debug commands do not have any aliases\n",
                                TextColors.DARK_AQUA, "Note: ", TextColors.AQUA, "These commands can have major consequences if used! If you are unsure exactly what you're doing, do not run any Debug command!\n",
                                TextColors.DARK_AQUA, "Permissions: ", TextColors.AQUA, "cmdcalendar.debug")))
                        .build())
                .append(Text.builder("<Confirm>")
                        .color(TextColors.AQUA)
                        .onHover(TextActions.showText(Text.of(
                                TextColors.DARK_AQUA, "Confirm: ", TextColors.AQUA, "Confirms running a debug command")))
                        .build())
                .build());
        if (CmdCalendar.getWiki() != null && CmdCalendar.getDiscord() != null) {
            src.sendMessage(Text.builder("| ")
                    .color(TextColors.DARK_AQUA)
                    .append(Text.builder("CmdCalendar Wiki")
                            .color(TextColors.AQUA).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(CmdCalendar.getWiki()))
                            .onHover(TextActions.showText(Text.of("Click to open the CmdCalendar Wiki")))
                            .build())
                    .append(Text.of(TextColors.DARK_AQUA, " | "))
                    .append(Text.builder("Support Discord")
                            .color(TextColors.AQUA).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(CmdCalendar.getDiscord()))
                            .onHover(TextActions.showText(Text.of("Click to open the Support Discord")))
                            .build())
                    .append(Text.of(TextColors.DARK_AQUA, " |"))
                    .build());
        }
        return CommandResult.success();
    }
}