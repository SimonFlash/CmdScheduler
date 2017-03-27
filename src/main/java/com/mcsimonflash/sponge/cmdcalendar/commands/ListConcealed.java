package com.mcsimonflash.sponge.cmdcalendar.commands;

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

public class ListConcealed implements CommandExecutor{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String cmdConfirm = args.<String>getOne("cmdConfirm").get();

        if (cmdConfirm.equalsIgnoreCase("Confirm")) {
            int c = 0;
            for (CmdCalTask ccTask : Tasks.getSortedTaskMap().values()) {
                if (ccTask.getStatus().equals(TaskStatus.Concealed)) {
                    src.sendMessage(Text.of(TextColors.DARK_AQUA, c++, ": ", TextColors.AQUA, ccTask.getName(), " [", ccTask.getType(), "] -> ", TextColors.GREEN, "Concealed"));
                }
            }
            return CommandResult.success();
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, "CmdCal ERROR: ", TextColors.RED, "Debug commands must be confirmed!"));
            return CommandResult.empty();
        }
    }
}
