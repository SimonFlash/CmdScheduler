package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_TaskList implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {;
        if (Manager_Tasks.getTaskList().isEmpty()) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
            return CommandResult.empty();
        } else {
            int c = 1;

            for (Object_CmdCalTask task : Manager_Tasks.getTaskList()) {
                if (task.getTaskStatus()) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getTaskName(), " (", task.getTaskInterval(), ") -> ", TextColors.GREEN, "Active"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getTaskName(), " (", task.getTaskInterval(), ") -> ", TextColors.RED, "Halted"));
                }
            }
        }
        return CommandResult.success();
    }
}