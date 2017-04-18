package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Util;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskStatus;
import com.mcsimonflash.sponge.cmdcalendar.objects.IntervalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.SchedulerTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class ShowTask implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();

        if (!Tasks.taskMap.containsKey(name)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " does not exist!"));
            return CommandResult.empty();
        }
        CmdCalTask ccTask = Tasks.taskMap.get(name);
        Util.refreshStatus(ccTask);
        src.sendMessage(Text.of(TextColors.DARK_AQUA, "Name: ", TextColors.AQUA, ccTask.Name));
        src.sendMessage(Text.of(TextColors.DARK_AQUA, "Type: ", TextColors.AQUA, ccTask.Type));
        if (ccTask instanceof SchedulerTask) {
            if (((SchedulerTask) ccTask).Schedule.isEmpty()) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "Schedule: ", TextColors.RED, "No schedule set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Schedule: ", TextColors.AQUA, ((SchedulerTask) ccTask).Schedule));
            }
        } else if (ccTask instanceof IntervalTask) {
            if (((IntervalTask) ccTask).Interval == -1) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "Interval: ", TextColors.RED, "No interval set!"));
            } else {
                src.sendMessage(Text.of(TextColors.DARK_AQUA, "Interval: ", TextColors.AQUA, ((IntervalTask) ccTask).Interval));
            }
        }
        if (ccTask.Command.isEmpty()) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Command: ", TextColors.RED, "No command set!"));
        } else {
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Command: ", TextColors.AQUA, "/", ccTask.Command));
        }
        if (ccTask.Description.isEmpty()) {
            src.sendMessage(Text.of(TextColors.GOLD, "Description: ", TextColors.YELLOW, "No description set!"));
        } else {
            src.sendMessage(Text.of(TextColors.DARK_AQUA, "Description: ", TextColors.AQUA, ccTask.Description));
        }
        TextColor color = TextColors.RED;
        switch (ccTask.Status) {
            case Active:
                color = TextColors.GREEN;
                break;
            case Concealed_Active:
            case Concealed_Halted:
                color = TextColors.GRAY;
                break;
            case Halted:
                color = TextColors.YELLOW;
                break;
            case Suspended:
                color = TextColors.GOLD;
                break;
        }
        String msg = ccTask.Status.equals(TaskStatus.ERROR) ? "Error - not configured properly" : Util.printStatus(ccTask.Status);
        src.sendMessage(Text.of(ccTask.Status.equals(TaskStatus.ERROR) ? TextColors.DARK_RED : TextColors.DARK_AQUA, "Status: ", color, msg));
        return CommandResult.success();
    }
}