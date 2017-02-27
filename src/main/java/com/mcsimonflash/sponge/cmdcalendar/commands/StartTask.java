package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StartTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();

        if (Tasks.verifyTask(taskName)) {
            if (!Tasks.getTask(taskName).getCommand().equalsIgnoreCase("")) {
                Tasks.startTask(taskName);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " Enabled. Current Interval: ", TextColors.DARK_GREEN, Tasks.getTask(taskName).getInterval()));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not have a command!"));
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist! /CmdCal CreateTask ", taskName, " to create task."));
        }

        return CommandResult.success();
    }
}