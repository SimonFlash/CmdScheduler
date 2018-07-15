package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Children;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Command;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.Permission;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.command.create.Create;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aliases({"cmdscheduler", "cs"})
@Permission("cmdscheduler.command.base")
@Children({Create.class, Delete.class, Execute.class, Info.class, List.class, Start.class, Stop.class})
public class Base extends Command {

    public static final Text TASK_ARG = CmdUtils.arg(true, "task", CmdUtils.info("Task", "The name of a task.\n", "String", "", ""));
    public static final Text LINKS = Text.of("                      ", CmdUtils.link("Ore Project", CmdScheduler.get().getContainer().getUrl().flatMap(CmdUtils::parseURL)), TextColors.GRAY, " | ", CmdUtils.link("Support Discord", CmdPlugin.DISCORD));

    @Inject
    private Base(Settings settings) {
        super(settings.usage(CmdUtils.usage("/cmdscheduler ", CmdUtils.info("CmdScheduler", "Opens the command reference menu.\n", "", "cmdscheduler, cs\n", "cmdscheduler.command.base"),
                CmdUtils.arg(true, "...", CmdUtils.info("Subcommand", "One of CmdScheduler's subcommands.\n", "create, delete, execute, info, list, start, stop\n", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        PaginationList.builder()
                .padding(Text.of(TextColors.GRAY, "="))
                .title(CmdScheduler.get().getPrefix())
                .contents(Stream.concat(Stream.of(this), getChildren().stream()).map(Command::getUsage).collect(Collectors.toList()))
                .footer(LINKS)
                .sendTo(src);
        return CommandResult.success();
    }

}