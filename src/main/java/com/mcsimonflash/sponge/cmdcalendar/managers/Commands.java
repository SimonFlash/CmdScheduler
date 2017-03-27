package com.mcsimonflash.sponge.cmdcalendar.managers;

public class Commands {
    public static boolean testCommandBlacklisted(String taskCommand) {
        String coreCommand;
        if (taskCommand.contains(" ")) {
            coreCommand = taskCommand.substring(0, taskCommand.indexOf(" "));
        } else {
            coreCommand = taskCommand;
        }
        return Config.getBlacklist().contains(coreCommand);
    }
}
