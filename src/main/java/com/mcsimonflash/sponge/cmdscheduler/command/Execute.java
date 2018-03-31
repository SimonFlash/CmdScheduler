package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

@Singleton
@Aliases("execute")
@Permission("cmdscheduler.command.execute.base")
public class Execute extends Command {

    @Inject
    private Execute(CommandService service) {
        super(service, settings()
                .arguments(Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task"))
                .usage(CmdUtils.usage("/cmdscheduler execute ", CmdUtils.info("Execute", "Executes a task.\n", "", "execute\n", "cmdscheduler.command.execute.base"), Base.TASK_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CommandTask task = args.<CommandTask>getOne("task").get();
        task.getTask().execute(CmdScheduler.get().getContainer());
        src.sendMessage(Text.of("Successfully executed task " + task.getName() + "."));
        return CommandResult.success();
    }

}