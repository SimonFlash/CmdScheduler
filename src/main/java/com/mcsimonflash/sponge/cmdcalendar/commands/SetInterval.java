package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetInterval implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        Integer taskInterval = args.<Integer>getOne("taskInterval").get();

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        } else {
            if (Tasks.getTask(taskName) instanceof Interval) {
                Interval intervalTask = (Interval) Tasks.getTask(taskName);
                if (intervalTask.getInterval() == taskInterval) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task intervals are identical!"));
                    return CommandResult.empty();
                } else {
                    intervalTask.setInterval(taskInterval);
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " interval successfully changed!"));
                    return CommandResult.success();
                }
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " is not an Interval task!"));
                return CommandResult.empty();
            }
        }
    }
}