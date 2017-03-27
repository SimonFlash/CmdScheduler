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

        if (Tasks.verifyTask(taskName)) {
            CmdCalTask ccTask = Tasks.getTask(taskName);
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Name: ", TextColors.AQUA, ccTask.getName()));
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Type: ", TextColors.AQUA, ccTask.getType()));

            if (ccTask instanceof Scheduler) {
                if (((Scheduler) ccTask).getSchedule().equals("")) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Schedule: No schedule set!"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, "Schedule: ", TextColors.AQUA, ((Scheduler) ccTask).getSchedule()));
                }
            } else if (ccTask instanceof Interval) {
                if (((Interval) ccTask).getInterval() == -1) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "Interval: No interval set!"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, "Interval: ", TextColors.AQUA, ((Interval) ccTask).getInterval()));
                }
            }

            if (ccTask.getCommand().equals("")) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "Command: ", TextColors.RED, "No command set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: /", ccTask.getCommand()));
            }

            if (ccTask.getDescription().equals("")) {
                src.sendMessage(Text.of(TextColors.GOLD, "Description: ", TextColors.YELLOW, "No description set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, ccTask.getDescription()));
            }

            if (ccTask.getStatus().equals(TaskStatus.Active)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GREEN, "Active"));
            } else if (ccTask.getStatus().equals(TaskStatus.Suspended)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.YELLOW, "Suspended"));
            } else if (ccTask.getStatus().equals(TaskStatus.Halted)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GOLD, "Halted"));
            } else if (ccTask.getStatus().equals(TaskStatus.Concealed)) {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Status: ", TextColors.GRAY, "Concealed"));
            } else if (ccTask.getStatus().equals(TaskStatus.ERROR)) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "Status: ", TextColors.RED, "Error - not configured properly"));
            }

            return CommandResult.success();
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}