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

public class SetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String taskCommand = args.<String>getOne("taskCommand").get();

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        } else {
            if (!Commands.testCommandExists(taskCommand)) { //SFZ: Implement
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, taskCommand, " is not valid!"));
                return CommandResult.empty();
            } else {
                if (!Config.isIgnoreBlacklistCheck() && Commands.testCommandBlacklisted(taskCommand)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, "Command is blacklisted!"));
                    return CommandResult.empty();
                }
                if (!Config.isIgnorePermissionCheck() && !Commands.testCommandPermission(src, taskCommand)) { //SFZ: Implement
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, "No permission for this command!"));
                    return CommandResult.empty();
                }

                Tasks.setTaskCommand(Tasks.getTask(taskName), taskCommand);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " command set!"));
                return CommandResult.success();
            }
        }
    }
}