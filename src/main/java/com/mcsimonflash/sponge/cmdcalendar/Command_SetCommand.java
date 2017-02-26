package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_SetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String taskCommand = args.<String>getOne("taskCommand").get();

        if (Manager_Tasks.verifyTask(taskName)) {
            if (Manager_Commands.verifyUnblockedCommand(taskCommand)) {
                Manager_Tasks.setTaskCommand(taskName, taskCommand);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " command changed to ", taskCommand));
                return CommandResult.success();
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: /", TextColors.RED, taskCommand, " is blocked!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}