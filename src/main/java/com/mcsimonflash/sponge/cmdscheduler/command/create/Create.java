package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.command.Base;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import com.mcsimonflash.sponge.cmdscheduler.task.CommandTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tristate;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Aliases("create")
@Permission("cmdscheduler.command.create.base")
@Children({Calendar.class, Classic.class, Cron.class})
public class Create extends Command {

    public static final Text
            NAME_ARG = CmdUtils.arg(true, "name", CmdUtils.info("Name", "The name of the task.\n", "String", "", "")),
            COMMAND_ARG = CmdUtils.arg(true, "command", CmdUtils.info("Command", "The command of the task.\n", "Command", "", "")),
            START_FLAG = CmdUtils.arg(false, "-start", CmdUtils.info("Start", "If defined, starts the task once created.\n", "", "-start", "")),
            ASYNC_FLAG = CmdUtils.arg(false, "-async", CmdUtils.info("Async", "If set to false, the task is processed synchronously.\n", "Boolean\n", "-async, -a", ""));

    @Inject
    private Create(CommandService service) {
        super(service, settings().usage(CmdUtils.usage("/cmdscheduler create ", CmdUtils.info("Create", "Creates a new task. The task is not saved to the config.\n", "", "create\n", "cmdscheduler.command.create.base"),
                CmdUtils.arg(true, "...", CmdUtils.info("Subcommand", "One of Create's subcommands.\n", "calendar, classic, cron", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        PaginationList.builder()
                .padding(Text.of(TextColors.GRAY, "="))
                .title(CmdScheduler.get().getPrefix())
                .contents(Stream.concat(Stream.of(this), getChildren().stream()).map(Command::getUsage).collect(Collectors.toList()))
                .footer(Base.LINKS)
                .sendTo(src);
        return CommandResult.success();
    }

    public static CommandResult create(CommandSource src, CommandContext args, Function<CommandContext, Schedule> function) throws CommandException {
        String name = args.<String>getOne("name").get();
        if (Config.tasks.containsKey(name.toLowerCase())) {
            throw new CommandException(Text.of("A task already exists with name " + name + "."));
        }
        try {
            CommandTask task = new CommandTask(name, args.<String>getOne("command").get(), function.apply(args), args.<Tristate>getOne("async").orElse(Tristate.UNDEFINED));
            Config.tasks.put(task.getName().toLowerCase(), task);
            src.sendMessage(Text.of("Successfully created task " + task.getName() + ". For this task to persist, it must be added to the config manually."));
            if (args.hasAny("start")) {
                task.getTask().start(CmdScheduler.get().getContainer());
                src.sendMessage(Text.of("Task " + task.getName() + " has been started."));
            }
            return CommandResult.success();
        } catch (IllegalArgumentException e) {
            throw new CommandException(Text.of(e.getMessage()));
        }
    }

}