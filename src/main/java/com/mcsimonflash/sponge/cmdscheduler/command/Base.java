package com.mcsimonflash.sponge.cmdscheduler.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.teslalibs.command.*;
import com.mcsimonflash.sponge.cmdscheduler.command.create.Create;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

@Singleton
@Aliases({"cmdscheduler", "cs"})
@Permission("cmdscheduler.command.base")
@Children({Create.class, Delete.class, Execute.class, Info.class, List.class, Start.class, Stop.class})
public class Base extends Command {

    @Inject
    private Base(CommandService service) {
        super(service, Settings.create());
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.success();
    }

}