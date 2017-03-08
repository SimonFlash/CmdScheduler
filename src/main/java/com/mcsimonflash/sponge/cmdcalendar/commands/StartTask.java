package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;

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

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "does not exist!"));
            return CommandResult.empty();
        } else {
            CmdCalTask cmdCalTask = Tasks.getTask(taskName);
            if (cmdCalTask.getCommand().equals("")) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "does not have a command!"));
                return CommandResult.empty();
            } else {
                if (cmdCalTask instanceof Scheduler) {
                    Scheduler schedulerTask = (Scheduler) cmdCalTask;
                    if (schedulerTask.getSchedule().equals("")) {
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "does not have a schedule!"));
                        return CommandResult.empty();
                    }
                } else if (cmdCalTask instanceof Interval) {
                    Interval intervalTask = (Interval) cmdCalTask;
                    if (intervalTask.getInterval() == -1) {
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "does not have an interval!"));
                        return CommandResult.empty();
                    }
                }

                Tasks.startTask(cmdCalTask);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " Enabled."));
                return CommandResult.success();
            }
        }
    }
}