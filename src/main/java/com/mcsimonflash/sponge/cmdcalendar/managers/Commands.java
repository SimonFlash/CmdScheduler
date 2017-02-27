package com.mcsimonflash.sponge.cmdcalendar.managers;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

public class Commands {
    public static boolean testCommandExists(String taskCommand) {
        return Sponge.getCommandManager().get(taskCommand).isPresent();
    }

    public static boolean testCommandNotBlacklisted(String taskCommand) {
        if (Config.isIgnoreBlacklistCheck()) {
            return true;
        }
        String coreCommand;
        if (taskCommand.contains(" ")) {
            coreCommand = taskCommand.substring(0, taskCommand.indexOf(' '));
        } else {
            coreCommand = taskCommand;
        }
        switch (coreCommand.toLowerCase()) {
            case "cc":
            case "cmdcal":
            case "commandcal":
            case "cmdcalendar":
            case "commandcalendar":
            case "start":
            case "stop":
            case "reload":
            case "op":
                return false;
        }
        return true;
    }

    public static boolean testCommandPermission(CommandSource src, String taskCommand) {
        return Sponge.getCommandManager().get(taskCommand).get().getCallable().testPermission(src);
    }
}
