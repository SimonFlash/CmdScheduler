package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Commands;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.Interval;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EditTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String variable = args.<String>getOne("variable").get();
        String value = args.<String>getOne("value").get();

        if (Tasks.verifyTask(taskName)) {
            if (RunTask.isActive(Tasks.getTask(taskName))) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " is currently running!"));
                return CommandResult.empty();
            } else {
                switch (variable.toLowerCase()) {
                    case "command":
                    case "cmd":
                    case "c":
                        if (value.length() > 1 && value.substring(0, 1).equals("/")) {
                            value = value.substring(1);
                        }
                        if (Config.isBlacklistCheck() && Commands.testCommandBlacklisted(value)) {
                            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Command is blacklisted!"));
                            return CommandResult.empty();
                        }
                        Tasks.getTask(taskName).setCommand(value);
                        Config.syncTask(Tasks.getTask(taskName));
                        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " command set!"));
                        return CommandResult.success();
                    case "description":
                    case "desc":
                    case "d":
                        Tasks.getTask(taskName).setDescription(value);
                        Config.syncTask(Tasks.getTask(taskName));
                        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " description set!"));
                        return CommandResult.success();
                    case "interval":
                    case "inter":
                    case "i":
                        if (Tasks.getTask(taskName) instanceof Interval) {
                            try {
                                int taskInterval = Integer.parseInt(value);
                                if (taskInterval < 1) {
                                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Interval is less than 1!"));
                                    return CommandResult.empty();
                                } else {
                                    ((Interval) Tasks.getTask(taskName)).setInterval(taskInterval);
                                    Config.syncTask(Tasks.getTask(taskName));
                                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " interval set!"));
                                    return CommandResult.success();
                                }
                            } catch (NumberFormatException ignored) {
                                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Interval was not numeric!"));
                                return CommandResult.empty();
                            }
                        } else {
                            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "is not an interval task!"));
                            return CommandResult.empty();
                        }
                    case "name":
                    case "n":
                        if (value.contains(" ")) {
                            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Name must not contain spaces!"));
                            return CommandResult.empty();
                        } else {
                            Tasks.getTask(taskName).setName(value);
                            Config.syncTask(Tasks.getTask(taskName));
                            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, "Name changed to ", value));
                            return CommandResult.success();
                        }
                    case "schedule":
                    case "sched":
                    case "s":
                        if (Tasks.getTask(taskName) instanceof Scheduler) {
                            String[] split = value.split(" ");
                            if (split.length != 5) {
                                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Schedule has incorrect number of values!"));
                                return CommandResult.empty();
                            }
                            for (String str : split) {
                                if (str.equals("*")) {
                                    break;
                                } else if (str.length() > 1 && str.substring(0, 1).equals("/")) {
                                    str = str.substring(1);
                                }
                                try {
                                    Integer.parseInt(str);
                                } catch (NumberFormatException ignored) {
                                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Schedule has incorrect format!"));
                                    return CommandResult.empty();
                                }
                            }

                            ((Scheduler) Tasks.getTask(taskName)).setSchedule(value);
                            Config.syncTask(Tasks.getTask(taskName));
                            src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " schedule set!"));
                            return CommandResult.success();
                        } else {
                            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, "is not an scheduler task!"));
                            return CommandResult.empty();
                        }
                    default:
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, variable, " is not valid!"));
                        return CommandResult.empty();
                }
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}