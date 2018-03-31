package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.schedule.ClassicSchedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

import java.util.function.Function;

@Singleton
@Aliases("classic")
@Permission("cmdscheduler.command.create.classic.base")
public class Classic extends Command {

    @Inject
    private Classic(CommandService service) {
        super(service, settings()
                .arguments(
                        Arguments.string().toElement("name"),
                        Arguments.flags()
                                .flag("start")
                                .flag(Arguments.tristate().toElement("async"), "async", "a")
                                .flag(Arguments.duration().toElement("delay"), "delay", "d")
                                .flag(Arguments.duration().toElement("interval"), "interval", "i")
                                .flag(Arguments.intObj().inRange(Range.atLeast(0)).toElement("delay-ticks"), "delay-ticks", "dt")
                                .flag(Arguments.intObj().inRange(Range.atLeast(0)).toElement("interval-ticks"), "interval-ticks", "it")
                                .build(),
                        Arguments.command().toElement("command"))
                .usage(CmdUtils.usage("/cmdscheduler create classic ", CmdUtils.info("Classic", "Creates a new task with a classic schedule.\n", "", "classic\n", "cmdscheduler.command.create.classic.base"), Create.NAME_ARG,
                        CmdUtils.arg(false, "-delay", CmdUtils.info("Delay", "The delay of this task.\n", "Duration (in the form #d#h#m#s#ms)\n", "-delay, -d", "")),
                        CmdUtils.arg(false, "-interval", CmdUtils.info("Interval", "The interval of this task.\n", "Duration (in the form #d#h#m#s#ms)\n", "-interval, -i", "")),
                        CmdUtils.arg(false, "-delay-ticks", CmdUtils.info("DelayTicks", "The delay of this task in ticks.\n", "Integer (at least 0)\n", "-delay-ticks, -dt", "")),
                        CmdUtils.arg(false, "-interval-ticks", CmdUtils.info("IntervalTicks", "The interval of this task in ticks.\n", "Integer (at least 0)\n", "-interval-ticks, -it", "")),
                        Create.ASYNC_FLAG, Create.START_FLAG, Create.COMMAND_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return Create.create(src, args, FUNCTION);
    }

    private static final Function<CommandContext, Schedule> FUNCTION = args -> {
        ClassicSchedule.Builder builder = ClassicSchedule.builder();
        args.<Integer>getOne("delay").ifPresent(builder::delay);
        args.<Integer>getOne("interval").ifPresent(builder::interval);
        args.<Integer>getOne("delay-ticks").ifPresent(builder::delayTicks);
        args.<Integer>getOne("interval-ticks").ifPresent(builder::intervalTicks);
        return builder.build();
    };

}