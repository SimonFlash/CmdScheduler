package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Util;
import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.IntervalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.SchedulerTask;
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
        String name = args.<String>getOne("name").get();
        String variable = args.<String>getOne("variable").get();
        String value = args.<String>getOne("value").get();

        if (!Tasks.taskMap.containsKey(name)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " does not exist!"));
            return CommandResult.empty();
        }
        CmdCalTask ccTask = Tasks.taskMap.get(name);
        if (Tasks.isActive(ccTask)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " is currently running!"));
            return CommandResult.empty();
        }
        switch (variable.toLowerCase()) {
            case "command":
                variable = "command";
                value = value.substring(0, 1).equals("/") ? value.substring(1) : value;
                if (Util.checkBlacklist(value)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Command is blacklisted!"));
                    return CommandResult.empty();
                }
                ccTask.Command = value;
                break;
            case "description":
                variable = "description";
                ccTask.Description = value;
                break;
            case "interval":
                variable = "interval";
                if (!(ccTask instanceof IntervalTask)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " is not an interval task!"));
                    return CommandResult.empty();
                }
                try {
                    int interval = Integer.parseInt(value);
                    if (interval < 1) {
                        src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Interval is less than 1!"));
                        return CommandResult.empty();
                    }
                    ((IntervalTask) ccTask).Interval = interval;
                } catch (NumberFormatException ignored) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Interval was not numeric!"));
                    return CommandResult.empty();
                }
                break;
            case "name":
                variable = "name";
                if (value.contains(" ")) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Name must not contain spaces!"));
                    return CommandResult.empty();
                }
                Config.deleteTask(ccTask);
                ccTask.Name = value;
                break;
            case "schedule":
                variable = "schedule";
                if (!(ccTask instanceof SchedulerTask)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " is not an scheduler task!"));
                    return CommandResult.empty();
                }
                if (value.length() - value.replace(" ", "").length() != 4) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Schedule has incorrect number of values!"));
                    return CommandResult.empty();
                }
                if (!Util.validateSchedule(value)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Schedule has incorrect format!"));
                    return CommandResult.empty();
                }
                ((SchedulerTask) ccTask).Schedule = value;
                break;
            default:
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, variable, " is not valid!"));
                return CommandResult.empty();
        }
        Config.writeTask(ccTask);
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, name, " ", variable, " set!"));
        return CommandResult.success();
    }
}