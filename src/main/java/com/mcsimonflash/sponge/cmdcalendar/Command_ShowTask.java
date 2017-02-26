package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_ShowTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();

        if (Manager_Tasks.verifyTask(taskName)) {
            Object_CmdCalTask task = Manager_Tasks.getTask(taskName);
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Task: ", TextColors.AQUA, task.getTaskName()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, task.getTaskDescription()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Interval: ", TextColors.AQUA, task.getTaskInterval()));
            if (task.getTaskCommand() != null) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: /", task.getTaskCommand()));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: ", TextColors.AQUA, "No command set!"));
            }
            if (task.getTaskStatus()) {
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