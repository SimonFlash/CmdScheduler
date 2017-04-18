package com.mcsimonflash.sponge.cmdcalendar.commands;

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

public class StartTask implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();

        if (!Tasks.taskMap.containsKey(name)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " does not exist!"));
            return CommandResult.empty();
        }
        CmdCalTask ccTask = Tasks.taskMap.get(name);
        Util.refreshStatus(ccTask);
        if (ccTask.Status.equals(TaskStatus.ERROR)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " status is locked to Error!"));
            return CommandResult.empty();
        } else if (ccTask.Status.equals(TaskStatus.Active) || ccTask.Status.equals(TaskStatus.Concealed_Active)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " is already running!"));
            return CommandResult.empty();
        }
        Util.startTask(ccTask);
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, name, " Enabled."));
        return CommandResult.success();
    }
}