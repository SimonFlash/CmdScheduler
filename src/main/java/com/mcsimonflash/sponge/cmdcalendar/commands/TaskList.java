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
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class TaskList implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        TaskStatus status = args.<String>getOne("status").isPresent() ? Util.parseStatus(args.<String>getOne("status").get()) : null;

        if (Tasks.taskMap.isEmpty()) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks exist!"));
            return CommandResult.empty();
        }
        if (status != null && (status.equals(TaskStatus.Concealed_Active) || status.equals(TaskStatus.Concealed_Halted))) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Concealed tasks must be viewed through /CmdCal Debug ListConcealed!"));
            return CommandResult.empty();
        }
        int c = 1;
        boolean foundTask = false;
        for (CmdCalTask ccTask : Tasks.taskMap.values()) {
            if ((ccTask.Status.equals(TaskStatus.Concealed_Active) || ccTask.Status.equals(TaskStatus.Concealed_Halted)) ||
                    (status != null && !ccTask.Status.equals(status))) {
                continue;
            }
            foundTask = true;
            TextColor color = TextColors.GRAY;
            switch (ccTask.Status) {
                case Active:
                    color = TextColors.GREEN;
                    break;
                case Halted:
                    color = TextColors.YELLOW;
                    break;
                case Suspended:
                    color = TextColors.GOLD;
                    break;
                case ERROR:
                    color = TextColors.RED;
            }
            src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, ccTask.Name, TextColors.DARK_AQUA,
                    " [", TextColors.AQUA, Util.printType(ccTask.Type), TextColors.DARK_AQUA, "] -> ", color, Util.printStatus(ccTask.Status)));
        }
        if (!foundTask) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "No tasks found!"));
            return CommandResult.empty();
        }
        return CommandResult.success();
    }
}