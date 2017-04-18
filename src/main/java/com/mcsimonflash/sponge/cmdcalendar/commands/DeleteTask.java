package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
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

public class DeleteTask implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();
        String confirm = args.<String>getOne("confirm").get();

        if (!Tasks.taskMap.containsKey(name)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, name, " does not exist!"));
            return CommandResult.empty();
        }
        if (!confirm.equalsIgnoreCase("confirm")) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "DeleteTask must be confirmed!"));
            return CommandResult.empty();
        }
        CmdCalTask ccTask = Tasks.taskMap.get(name);
        if (ccTask.Status.equals(TaskStatus.Active)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task must be halted before deleting!"));
            return CommandResult.empty();
        }
        Tasks.taskMap.remove(name);
        Config.deleteTask(ccTask);
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, name, " removed from tasklist!"));
        return CommandResult.success();
    }
}