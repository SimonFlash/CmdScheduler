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
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class TaskList implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (Tasks.getSortedTaskMap().isEmpty()) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
            return CommandResult.empty();
        } else {
            int c = 1;
            for (CmdCalTask ccTask : Tasks.getSortedTaskMap().values()) {
                TextColor statusColor;
                if (ccTask.getStatus().equals(TaskStatus.Concealed)) {
                    break;
                }
                switch (ccTask.getStatus()) {
                    case Active:
                        statusColor = TextColors.GREEN;
                        break;
                    case Halted:
                        statusColor = TextColors.YELLOW;
                        break;
                    case Suspended:
                        statusColor = TextColors.DARK_GREEN;
                        break;
                    default:
                        statusColor = TextColors.RED;
                }
                src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", ccTask.getName(), " [", TextColors.AQUA, ccTask.getType(), TextColors.DARK_AQUA, "] -> ", statusColor, "Active"));
            }
            return CommandResult.success();
        }
    }
}