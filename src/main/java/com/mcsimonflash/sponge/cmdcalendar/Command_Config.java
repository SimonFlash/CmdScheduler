package com.mcsimonflash.sponge.cmdcalendar;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Command_Config implements CommandExecutor {

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String cmdParam = args.<String>getOne("parameter").get();
        String cmdConfirm = args.<String>getOne("cmdConfirm").get();

        switch (cmdParam.toLowerCase()) {
            case "save":
                if (cmdConfirm.equalsIgnoreCase("Confirm")) {
                    Manager_Config.writeConfig();
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks saved to config!"));
                    return CommandResult.success();
                }
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Command not confirmed!"));
                return CommandResult.empty();
            case "load":
                if (cmdConfirm.equalsIgnoreCase("Confirm")) {
                    Manager_Config.writeConfig();
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks loaded from config!"));
                    return CommandResult.success();
                }
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Command not confirmed!"));
                return CommandResult.empty();
            default:
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Config subcommand not recognized!"));
                return CommandResult.empty();
        }
    }
}
