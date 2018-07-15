package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

@Aliases("execute")
@Permission("cmdscheduler.command.execute.base")
public class Execute extends Command {

    @Inject
    private Execute(Settings settings) {
        super(settings.usage(CmdUtils.usage("/cmdscheduler execute ", CmdUtils.info("Execute", "Executes a task.\n", "", "execute\n", "cmdscheduler.command.execute.base"), Base.TASK_ARG))
                .elements(Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task")));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CommandTask task = args.<CommandTask>getOne("task").get();
        task.getTask().execute(CmdScheduler.get().getContainer());
        src.sendMessage(CmdScheduler.getMessage(src, "cmdscheduler.command.execute.success", "task", task.getName()));
        return CommandResult.success();
    }

}