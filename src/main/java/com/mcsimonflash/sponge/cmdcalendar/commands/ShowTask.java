package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ShowTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist! /CmdCal CreateTask ", taskName, " to create task."));
            return CommandResult.empty();
        } else {
            CmdCalTask cmdCalTask = Tasks.getTask(taskName);
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Task: ", TextColors.AQUA, cmdCalTask.getName()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Type: ", TextColors.AQUA, cmdCalTask.getType()));

            if (cmdCalTask instanceof Scheduler) {
                Scheduler schedulerTask = (Scheduler) cmdCalTask;
                if (schedulerTask.getSchedule().equals("")) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Schedule: No schedule set!"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Schedule: ", TextColors.RED, schedulerTask.getSchedule()));
                }
            } else if (cmdCalTask instanceof Interval) {
                Interval intervalTask = (Interval) cmdCalTask;
                if (intervalTask.getInterval() == -1) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Interval: No interval set!"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Schedule: ", TextColors.RED, intervalTask.getInterval()));
                }
            }

            if (cmdCalTask.getCommand().equals("")) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: ", TextColors.AQUA, "No command set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: /", cmdCalTask.getCommand()));
            }

            if (cmdCalTask.getDescription().equals("")) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, "No description set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, cmdCalTask.getDescription()));
            }

            if (cmdCalTask.getStatus().equals(TaskStatus.Active)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GREEN, "Active"));
            } else if (cmdCalTask.getStatus().equals(TaskStatus.Suspended)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.YELLOW, "Suspended"));
            } else if (cmdCalTask.getStatus().equals(TaskStatus.Halted)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.RED, "Halted"));
            } else if (cmdCalTask.getStatus().equals(TaskStatus.Concealed)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.WHITE, "Concealed"));
            }

            return CommandResult.success();
        }
    }
}