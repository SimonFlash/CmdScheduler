package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.Scheduler;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetSchedule implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String taskSchedule = args.<String>getOne("taskSchedule").get();

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        } else {
            if (Tasks.getTask(taskName) instanceof Scheduler) {
                Scheduler schedulerTask = (Scheduler) Tasks.getTask(taskName);
                if (((Scheduler) Tasks.getTask(taskName)).getSchedule() == taskSchedule) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal WARNING: ", TextColors.YELLOW, "Task schedules are identical!"));
                    return CommandResult.empty();
                } else {
                    Tasks.setSchedule(schedulerTask, taskSchedule);
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " schedule successfully changed!"));
                    return CommandResult.success();
                }
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " is not an Scheduler task!"));
                return CommandResult.empty();
            }
        }
    }
}