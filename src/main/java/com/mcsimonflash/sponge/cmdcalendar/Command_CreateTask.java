package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_CreateTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        int taskInterval = args.<Integer>getOne("taskInterval").get();
        String taskCommand = args.<String>getOne("taskCommand").get();

        if (!Manager_Tasks.verifyTask(taskName)) {
            if (taskInterval >= 1) {
                if (taskCommand.substring(0, 1).equals("/")) {
                    taskCommand = taskCommand.replaceFirst("/", "");
                }
                if (Manager_Commands.verifyUnblockedCommand(taskCommand)) {
                    Manager_Tasks.addTask(taskName, taskInterval, taskCommand);
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " created!"));
                    return CommandResult.success();
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, taskCommand, " is blocked!"));
                    return CommandResult.empty();
                }
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task interval (", taskInterval, ") less than 1!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " already exists!"));
            return CommandResult.empty();
        }
    }
}