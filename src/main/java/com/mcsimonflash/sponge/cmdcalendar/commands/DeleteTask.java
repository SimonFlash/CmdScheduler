package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.RunTask;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
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
        String taskName = args.<String>getOne("taskName").get();
        String cmdConfirm = args.<String>getOne("cmdConfirm").get();

        if (Tasks.verifyTask(taskName)) {
            if (cmdConfirm.equalsIgnoreCase("Confirm")) {
                if (Tasks.getTask(taskName).getStatus().equals(CmdCalTask.TaskStatus.Active)) {
                    src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task must not be active!"));
                    return CommandResult.empty();
                } else {
                    Config.deleteTask(Tasks.getTask(taskName));
                    RunTask.removeTask(Tasks.getTask(taskName));
                    Tasks.removeTask(taskName);
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS", TextColors.GREEN, taskName, " removed from tasklist!"));
                    return CommandResult.success();
                }
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "DeleteTask must be confirmed!"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        }
    }
}