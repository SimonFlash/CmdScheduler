package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdscheduler.CmdScheduler;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.stream.Collectors;

@Singleton
@Aliases("list")
@Permission("cmdscheduler.command.list.base")
public class List extends Command {

    @Inject
    private List(CommandService service) {
        super (service, Settings.create());
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        (src instanceof Player ? PaginationList.builder() : PaginationList.builder().linesPerPage(-1))
                .title(CmdScheduler.get().Prefix)
                .padding(Text.of(TextColors.GRAY, "="))
                .contents(Config.tasks.values().stream()
                        .map(t -> Text.of(" - ").concat(Text.builder(t.getName())
                                .style(TextStyles.UNDERLINE)
                                .onHover(TextActions.showText(Text.of("Click for more information")))
                                .onClick(TextActions.runCommand("/cs info " + t.getName()))
                                .build()))
                        .collect(Collectors.toList()))
                .sendTo(src);
        return CommandResult.success();
    }

}