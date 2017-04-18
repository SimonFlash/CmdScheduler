package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Util;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Debug implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String subcommand = args.<String>getOne("subcommand").get();
        String confirm = args.<String>getOne("confirm").get();

        if (!confirm.equalsIgnoreCase("confirm")) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Debug commands must be confirmed!"));
        }
        switch (subcommand.toLowerCase()) {
            case "listconcealed":
                if (Tasks.taskMap.isEmpty()) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
                    return CommandResult.empty();
                }
                int c = 1;
                boolean foundTask = false;
                for (String name : Util.getTaskNames(true)) {
                    CmdCalTask ccTask = Tasks.taskMap.get(name);
                    if (ccTask.Status.equals(TaskStatus.Concealed_Active) || ccTask.Status.equals(TaskStatus.Concealed_Halted)) {
                        foundTask = true;
                        src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, ccTask.Name, TextColors.DARK_AQUA, " [", TextColors.AQUA, ccTask.Type, TextColors.DARK_AQUA, "] -> ",
                                ccTask.Status.equals(TaskStatus.Concealed_Active) ? TextColors.GREEN : TextColors.YELLOW, Util.printStatus(ccTask.Status)));
                    }
                }
                if (!foundTask) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No concealed tasks exist!"));
                }
                return CommandResult.success();
            case "loadconfig":
                if (!Tasks.isActiveTasksMapEmpty()) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Tasks are still running!"));
                    return CommandResult.empty();
                }
                Config.readConfig();
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks loaded from config!"));
                return CommandResult.success();
            case "reloadtasks":
                if (Tasks.taskMap.isEmpty()) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
                    return CommandResult.empty();
                }
                Tasks.reloadTasks();
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks has been reloaded!"));
                return CommandResult.success();
            case "saveconfig":
                Config.writeConfig();
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Tasks saved to config!"));
                return CommandResult.success();
            case "stopall":
                if (Tasks.taskMap.isEmpty()) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No active tasks exist!"));
                    return CommandResult.empty();
                }
                Tasks.stopAll();
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "All tasks successfully stopped!"));
                return CommandResult.success();
            case "unconceal":
                if (Tasks.taskMap.isEmpty()) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
                    return CommandResult.empty();
                }
                foundTask = false;
                for (CmdCalTask ccTask : Tasks.taskMap.values()) {
                    if (ccTask.Status.equals(TaskStatus.Concealed_Active) || ccTask.Status.equals(TaskStatus.Concealed_Halted)) {
                        ccTask.Status = ccTask.Status.equals(TaskStatus.Concealed_Active) ? TaskStatus.Active : TaskStatus.Halted;
                        foundTask = true;
                    }
                }
                if (foundTask) {
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "All tasks are no longer concealed!"));
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "No concealed tasks exist!"));
                }
                return CommandResult.success();
            default:
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Subcommand not recognized!"));
                return CommandResult.empty();
        }
    }
}
