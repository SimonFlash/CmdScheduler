package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;

import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetDescription implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        String taskDescription = args.<String>getOne("taskDescription").get();

        if (!Tasks.verifyTask(taskName)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " does not exist!"));
            return CommandResult.empty();
        } else {
            CmdCalTask cmdCalTask = Tasks.getTask(taskName);
            if (cmdCalTask.getDescription().equalsIgnoreCase(taskDescription)) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task descriptions are case insensitive!"));
                return CommandResult.empty();
            } else {
                if (taskName.equalsIgnoreCase(taskDescription)) {
                    src.sendMessage(Text.of(TextColors.GOLD, "CmdCal WARNING: ", TextColors.YELLOW, "Task description is case insensitive from task name!"));
                }

                cmdCalTask.setDescription(taskDescription);
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " description sucessfully changed!"));
                return CommandResult.success();
            }
        }
    }
}