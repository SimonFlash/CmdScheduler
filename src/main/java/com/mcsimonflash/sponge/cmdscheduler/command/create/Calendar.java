package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.CommandParser;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.arguments.Arguments;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.schedule.CalendarSchedule;
import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

@Singleton
@Aliases("calendar")
@Permission("cmdscheduler.command.create.calendar.base")
public class Calendar extends Command {

    @Inject
    private Calendar(CommandService service) {
        super(service, Settings.create().arguments(
                Arguments.string().toElement("name"),
                Arguments.flags()
                        .flag("start")
                        //TODO: Commands cannot be set async. Should we use a -sync true/false flag instead?
                        .flag(Arguments.tristate().toElement("async"), "async", "a")
                        .flag(Arguments.duration().toElement("inverval"), "interval", "i")
                        .flag(Arguments.string().toElement("date"), "date", "d")
                        .build(),
                new CommandParser(ImmutableMap.of()).toElement("command")
        ));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();
        if (Config.tasks.containsKey(name.toLowerCase())) {
            throw new CommandException(Text.of("A task already exists with name " + name + "."));
        }
        try {
            CalendarSchedule.Builder builder = CalendarSchedule.builder();
            args.<String>getOne("date").ifPresent(builder::date);
            args.<Integer>getOne("interval").ifPresent(builder::interval);
            AdvancedTask task = AdvancedTask.builder()
                    .name(name)
                    .command(args.<String>getOne("command").get())
                    .schedule(builder.build())
                    .async(args.<Tristate>getOne("async").orElse(Tristate.UNDEFINED))
                    .build();
            Config.tasks.put(task.getName().toLowerCase(), task);
            src.sendMessage(Text.of("Successfully created task " + task.getName() + "."));
            if (args.hasAny("start")) {
                task.start(CmdScheduler.get().Container);
                src.sendMessage(Text.of("Task " + task.getName() + " has been started."));
            }
            return CommandResult.success();
        } catch (IllegalArgumentException e) {
            throw new CommandException(Text.of(e.getMessage()));
        }
    }

}