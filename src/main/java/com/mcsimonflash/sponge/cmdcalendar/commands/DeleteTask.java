package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeleteTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();

        if (Tasks.removeTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_GREEN, taskName, TextColors.GREEN, " removed from tasklist."));
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
        }

        return CommandResult.success();
    }
}