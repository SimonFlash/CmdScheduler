package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.arguments.Arguments;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

@Singleton
@Aliases("delete")
@Permission("cmdscheduler.command.delete.base")
public class Delete extends Command {

    @Inject
    private Delete(CommandService service) {
        super(service, Settings.create().arguments(
                Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task")
        ));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        AdvancedTask task = args.<AdvancedTask>getOne("task").get();
        task.stop(CmdScheduler.get().Container);
        Config.tasks.remove(task.getName().toLowerCase());
        src.sendMessage(Text.of("Successfully deleted task " + task.getName() + "."));
        return CommandResult.success();
    }

}