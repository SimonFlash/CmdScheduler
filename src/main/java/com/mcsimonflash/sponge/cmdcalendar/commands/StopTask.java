package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.managers.Util;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StopTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("taskName").get();

        if (!Tasks.taskMap.containsKey(name)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " does not exist!"));
            return CommandResult.empty();
        }
        CmdCalTask ccTask = Tasks.taskMap.get(name);
        if (!ccTask.Status.equals(CmdCalTask.TaskStatus.Active) && !ccTask.Status.equals(CmdCalTask.TaskStatus.Concealed_Active)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, ccTask.Name, " is not running!"));
            return CommandResult.empty();
        }
        Util.stopTask(ccTask);
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, ccTask.Name, " Disabled."));
        return CommandResult.success();
    }
}