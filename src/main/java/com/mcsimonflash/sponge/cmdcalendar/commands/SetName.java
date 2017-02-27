package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetName implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String newTaskName = args.<String>getOne("newTaskName").get();

        if (Tasks.verifyTask(taskName)) {
            if (!Tasks.verifyTask(newTaskName)) {
                if (Tasks.getTask(taskName).getName().equalsIgnoreCase(newTaskName)) {
                    src.sendMessage(Text.of(TextColors.GOLD, "CmdCal WARNING: ", TextColors.YELLOW, "Task names are case insensitive!"));
                }
                Tasks.setTaskName(taskName, newTaskName);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " name changed to ", newTaskName));
                return CommandResult.success();
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, newTaskName, " already exists!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}