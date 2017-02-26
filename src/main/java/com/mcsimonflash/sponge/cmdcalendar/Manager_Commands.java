package com.mcsimonflash.sponge.cmdcalendar;

public class Manager_Commands {
    public static boolean verifyUnblockedCommand(String taskCommand) {
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
}
