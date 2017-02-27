package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Commands;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        int taskInterval = args.<Integer>getOne("taskInterval").get();
        String taskCommand = args.<String>getOne("taskCommand").get();

        if (!Tasks.verifyTask(taskName)) {
            if (taskInterval >= 1) {
                if (taskCommand.substring(0, 1).equals("/")) {
                    taskCommand = taskCommand.replaceFirst("/", "");
                }
                if (Commands.testCommandExists(taskCommand)) {
                    if (!Config.isIgnoreBlacklistCheck() && Commands.testCommandNotBlacklisted(taskCommand)) {
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, taskCommand, " is blocked!"));
                        return CommandResult.empty();
                    }
                    if (!Config.isIgnorePermissionCheck() && !Commands.testCommandPermission(src, taskCommand)) {
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, "No permission for this command!"));
                        return CommandResult.empty();
                    }

                    Tasks.addTask(taskName, taskInterval, taskCommand);
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " created!"));
                    return CommandResult.success();
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, taskCommand, " is not valid!"));
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