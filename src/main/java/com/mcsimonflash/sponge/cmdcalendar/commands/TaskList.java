package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;

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
                if (task.getStatus().equals(TaskStatus.Active)) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getName(), " [", task.getType(), "] -> ", TextColors.GREEN, "Active"));
                } else if (task.getStatus().equals(TaskStatus.Suspended)) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getName(), " [", task.getType(), "] -> ", TextColors.YELLOW, "Suspended"));
                } else if (task.getStatus().equals(TaskStatus.Halted)) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, task.getName(), " [", task.getType(), "] -> ", TextColors.RED, "Halted"));
                }
            }
            return CommandResult.success();
        }
    }
}