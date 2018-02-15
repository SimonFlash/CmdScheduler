package com.mcsimonflash.sponge.cmdscheduler.command.create;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

@Singleton
@Aliases("create")
@Permission("cmdscheduler.command.create.base")
@Children({Calendar.class, Classic.class, Cron.class})
public class Create extends Command {

    @Inject
    private Create(CommandService service) {
        super(service, Settings.create());
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.success();
    }

}