package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.schedule.CronSchedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Units;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

import java.util.function.Function;

@Singleton
@Aliases("cron")
@Permission("cmdscheduler.command.create.cron.base")
public class Cron extends Command {

    @Inject
    private Cron(CommandService service) {
        super(service, settings()
                .arguments(
                        Arguments.string().toElement("name"),
                        Arguments.flags()
                                .flag("start")
                                .flag(Arguments.tristate().toElement("async"), "async", "a")
                                .flag(Arguments.string().toElement("schedule"), "schedule", "sc")
                                .flag(Arguments.string().toElement("months"), "months", "mo")
                                .flag(Arguments.string().toElement("days"), "days", "d")
                                .flag(Arguments.string().toElement("hours"), "hours", "h")
                                .flag(Arguments.string().toElement("minutes"), "minutes", "m")
                                .flag(Arguments.string().toElement("seconds"), "seconds", "s")
                                .flag(Arguments.string().toElement("milliseconds"), "milliseconds", "ms")
                                .build(),
                        Arguments.command().toElement("command"))
                .usage(CmdUtils.usage("/cmdscheduler create cron ", CmdUtils.info("Cron", "Creates a new task with a cron schedule.\n", "", "cron\n", "cmdscheduler.command.create.cron.base"), Create.NAME_ARG,
                        CmdUtils.arg(false, "-schedule", CmdUtils.info("Schedule", "The cron schedule of the task.\n", "Cron Schedule (in the form ms s m h d mo)\n", "-schedule, -sc", "")),
                        CmdUtils.arg(false, "-months", CmdUtils.info("Months", "The months schedule.\n", "Modifier (in range 1-12)\n", "-months, -mo", "")),
                        CmdUtils.arg(false, "-days", CmdUtils.info("Days", "The days schedule.\n", "Modifier (in range 1-31)\n", "-days, -d", "")),
                        CmdUtils.arg(false, "-hours", CmdUtils.info("Hours", "The hours schedule.\n", "Modifier (in range 0-23)\n", "-hours, -h", "")),
                        CmdUtils.arg(false, "-minutes", CmdUtils.info("Minutes", "The minutes schedule.\n", "Modifier (in range 0-59)\n", "-minutes, -m", "")),
                        CmdUtils.arg(false, "-seconds", CmdUtils.info("Seconds", "The seconds schedule.\n", "Modifier (in range 0-59)\n", "-seconds, -s", "")),
                        CmdUtils.arg(false, "-milliseconds", CmdUtils.info("Milliseconds", "The milliseconds schedule.\n", "Modifier (in range 0-999)\n", "-milliseconds, -ms", "")),
                        Create.ASYNC_FLAG, Create.START_FLAG, Create.COMMAND_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return Create.create(src, args, FUNCTION);
    }

    private static final Function<CommandContext, Schedule> FUNCTION = args -> {
        CronSchedule.Builder builder = CronSchedule.builder();
        if (args.hasAny("schedule")) {
            builder.schedule(args.<String>getOne("schedule").get());
        } else {
            for (Units unit : Units.values()) {
                args.<String>getOne(unit.name().toLowerCase()).ifPresent(m -> builder.setUnit(unit, m));
            }
        }
        return builder.build();
    };

}