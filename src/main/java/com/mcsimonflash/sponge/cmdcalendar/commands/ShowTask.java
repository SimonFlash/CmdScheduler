package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;

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

        if (Tasks.verifyTask(taskName)) {
            CmdCalTask task = Tasks.getTask(taskName);
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Task: ", TextColors.AQUA, task.getName()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, task.getDescription()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Interval: ", TextColors.AQUA, task.getInterval()));
            if (task.getCommand() != null) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: /", task.getCommand()));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: ", TextColors.AQUA, "No command set!"));
            }
            if (task.getStatus()) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GREEN, "Active"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GOLD, "Halted"));
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist! /CmdCal CreateTask ", taskName, " to create task."));
        }

        return CommandResult.success();
    }
}