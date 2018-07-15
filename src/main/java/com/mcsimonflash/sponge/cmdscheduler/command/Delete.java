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

@Aliases("delete")
@Permission("cmdscheduler.command.delete.base")
public class Delete extends Command {

    @Inject
    private Delete(Settings settings) {
        super(settings.usage(CmdUtils.usage("/cmdscheduler delete ", CmdUtils.info("Delete", "Deletes a task. If the task is defined in the config, it must be deleted there manually.\n", "", "delete\n", "cmdscheduler.command.delete.base"), Base.TASK_ARG))
                .elements(Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task")));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CommandTask task = args.<CommandTask>getOne("task").get();
        task.getTask().stop(CmdScheduler.get().getContainer());
        Config.tasks.remove(task.getName().toLowerCase());
        src.sendMessage(CmdScheduler.getMessage(src, "cmdscheduler.command.delete.success", "task", task.getName()));
        return CommandResult.success();
    }

}