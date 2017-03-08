package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class LoadConfig implements CommandExecutor {

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String cmdConfirm = args.<String>getOne("cmdConfirm").get();

        if (!cmdConfirm.equalsIgnoreCase("Confirm")) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Debug commands must be confirmed!"));
            return CommandResult.empty();
        } else {
            Config.readConfig();
            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks loaded from config!"));
            return CommandResult.success();
        }
    }
}