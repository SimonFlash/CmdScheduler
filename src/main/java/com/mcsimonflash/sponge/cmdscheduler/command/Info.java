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
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

@Singleton
@Aliases("info")
@Permission("cmdscheduler.command.info.base")
public class Info extends Command {

    @Inject
    private Info(CommandService service) {
        super(service, settings()
                .arguments(Arguments.choices(Config.tasks, ImmutableMap.of("no-choice", "Input <arg> is not the name of a task!")).toElement("task"))
                .usage(CmdUtils.usage("/cmdscheduler info ", CmdUtils.info("Info", "Shows information about a task (primarily a debugging tool)\n", "", "info\n", "cmdscheduler.command.info.base"), Base.TASK_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        src.sendMessage(Text.of(args.<CommandTask>getOne("task").get().getTask()));
        return CommandResult.success();
    }

}