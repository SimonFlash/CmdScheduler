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

public class TaskList implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (Tasks.getTaskList().isEmpty()) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
            return CommandResult.empty();
        } else {
            int c = 1;

            for (CmdCalTask task : Tasks.getTaskList()) {
                if (task.getStatus()) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getName(), " (", task.getInterval(), ") -> ", TextColors.GREEN, "Active"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getName(), " (", task.getInterval(), ") -> ", TextColors.RED, "Halted"));
                }
            }
        }
        return CommandResult.success();
    }
}