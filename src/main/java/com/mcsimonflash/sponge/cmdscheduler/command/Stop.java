package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

@Singleton
@Aliases("stop")
@Permission("cmdscheduler.command.stop.base")
public class Stop extends Command {

    @Inject
    private Stop(CommandService service) {
        super(service, settings()
                .arguments(Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task"))
                .usage(CmdUtils.usage("/cmdscheduler stop ", CmdUtils.info("Stop", "Stops a task.\n", "", "stop\n", "cmdscheduler.command.stop.base"), Base.TASK_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CommandTask task = args.<CommandTask>getOne("task").get();
        task.getTask().stop(CmdScheduler.get().getContainer());
        src.sendMessage(Text.of("Successfully stopped task " + task.getName() + "."));
        return CommandResult.success();
    }

}