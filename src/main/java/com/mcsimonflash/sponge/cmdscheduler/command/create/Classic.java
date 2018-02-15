package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.CommandParser;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.arguments.Arguments;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.schedule.ClassicSchedule;
import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

@Singleton
@Aliases("classic")
@Permission("cmdscheduler.command.create.classic.base")
public class Classic extends Command {

    @Inject
    private Classic(CommandService service) {
        super(service, Settings.create().arguments(
                Arguments.string().toElement("name"),
                Arguments.flags()
                        .flag("start")
                        .flag(Arguments.tristate().toElement("async"), "async", "a")
                        .flag(Arguments.duration().toElement("delay"), "delay", "d")
                        .flag(Arguments.duration().toElement("inverval"), "interval", "i")
                        .flag(Arguments.integer().inRange(Range.atLeast(0)).toElement("delay-ticks"), "delay-ticks", "dt")
                        .flag(Arguments.integer().inRange(Range.atLeast(0)).toElement("interval-ticks"), "interval-ticks", "it")
                        .build(),
                new CommandParser(ImmutableMap.of()).toElement("command")
        ));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();
        if (Config.tasks.containsKey(name)) {
            throw new CommandException(Text.of("A task already exists with name " + name + "."));
        }
        try {
            ClassicSchedule.Builder builder = ClassicSchedule.builder();
            args.<Integer>getOne("delay").ifPresent(builder::delay);
            args.<Integer>getOne("interval").ifPresent(builder::interval);
            args.<Integer>getOne("delay-ticks").ifPresent(builder::delayTicks);
            args.<Integer>getOne("interval-ticks").ifPresent(builder::intervalTicks);
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