package com.mcsimonflash.sponge.cmdcalendar.commands;

import com.mcsimonflash.sponge.cmdcalendar.managers.Config;
import com.mcsimonflash.sponge.cmdcalendar.managers.Tasks;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask;
import com.mcsimonflash.sponge.cmdcalendar.objects.CmdCalTask.TaskType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateTask implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String taskName = args.<String>getOne("taskName").get();
        TaskType taskType = CmdCalTask.parseType(args.<String>getOne("taskType").get());

        if (taskType.equals(TaskType.ERROR)) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Task type does not exist!"));
            return CommandResult.empty();
        } else {
            if (Tasks.verifyTask(taskName)) {
                src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, taskName, " already exists!"));
                return CommandResult.empty();
            } else {
                Config.syncTask(Tasks.newTask(taskName, taskType));
                src.sendMessage(Text.of(TextColors.DARK_GREEN, "CmdCal SUCCESS: ", TextColors.GREEN, taskName, " created!"));
                return CommandResult.success();
            }
        }
    }
}