package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.RunTask;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StopAll implements CommandExecutor{

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String cmdConfirm = args.<String>getOne("cmdConfirm").get();

        if (!cmdConfirm.equalsIgnoreCase("Confirm")) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Debug commands must be confirmed!"));
            return CommandResult.empty();
        } else {
            RunTask.delAll();
            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "All tasks successfully stopped!"));
            return CommandResult.success();
        }
    }
}
