package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.schedule.CalendarSchedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

import java.util.function.Function;

@Singleton
@Aliases("calendar")
@Permission("cmdscheduler.command.create.calendar.base")
public class Calendar extends Command {

    @Inject
    private Calendar(CommandService service) {
        super(service, settings()
                .arguments(
                        Arguments.string().toElement("name"),
                        Arguments.flags()
                                .flag("start")
                                //TODO: Commands cannot be set async. Should we use a -sync true/false flag instead?
                                .flag(Arguments.tristate().toElement("async"), "async", "a")
                                .flag(Arguments.string().toElement("date"), "date", "d")
                                .flag(Arguments.duration().toElement("interval"), "interval", "i")
                                .build(),
                        Arguments.command().toElement("command"))
                .usage(CmdUtils.usage("/cmdscheduler create calendar ", CmdUtils.info("Calendar", "Create a new task with a calendar schedule.\n", "", "calendar\n", "cmdscheduler.command.create.calendar.base"), Create.NAME_ARG,
                        CmdUtils.arg(false, "-date", CmdUtils.info("Date", "The calendar date to execute this task on.\n", "Date (in the form yyyy-MM-dd/HH:mm:ss.SSS)\n", "-date, -d", "")),
                        CmdUtils.arg(false, "-interval", CmdUtils.info("Interval", "The interval of this task.\n", "Duration (in the form #d#h#m#s#ms)\n", "-interval, -i", "")),
                        Create.ASYNC_FLAG, Create.START_FLAG, Create.COMMAND_ARG)));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return Create.create(src, args, FUNCTION);
    }

    private static final Function<CommandContext, Schedule> FUNCTION = args -> {
        CalendarSchedule.Builder builder = CalendarSchedule.builder();
        args.<String>getOne("date").ifPresent(builder::date);
        args.<Integer>getOne("interval").ifPresent(builder::interval);
        return builder.build();
    };

}