package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_SetDescription implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String taskDescription = args.<String>getOne("taskDescription").get();

        if (Manager_Tasks.verifyTask(taskName)) {
            if (taskName.equalsIgnoreCase(taskDescription)) {
                src.sendMessage(Text.of(TextColors.GOLD, "CmdCal WARNING: ", TextColors.YELLOW, "Task description is case insensitive from task name!"));
            }
            if (Manager_Tasks.getTask(taskName).getTaskDescription().equalsIgnoreCase(taskDescription)) {
                src.sendMessage(Text.of(TextColors.GOLD, "CmdCal WARNING: ", TextColors.YELLOW, "Task descriptions are case insensitive!"));
            }
            Manager_Tasks.setTaskDescription(taskName, taskDescription);
            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " description changed to ", taskDescription));
            return CommandResult.success();
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}