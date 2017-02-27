package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

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

        if (Tasks.verifyTask(taskName)) {
            if (Tasks.getTask(taskName).getInterval() == taskInterval) {
                src.sendMessage(Text.of(TextColors.GOLD, "CmdCal WARNING: ", TextColors.YELLOW, "Task intervals are identical!"));
            }
            Tasks.setTaskInterval(taskName, taskInterval);
            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " interval changed to ", taskInterval));
            return CommandResult.success();
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}