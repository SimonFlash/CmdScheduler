package com.mcsimonflash.sponge.cmdscheduler;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.cmdscheduler.command.Base;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "cmdscheduler", name = "CmdScheduler", version = "1.0.0", authors = "Simon_Flash")
public class CmdScheduler extends CmdPlugin {

    private static CmdScheduler instance;

    @Inject
    public CmdScheduler(PluginContainer container) {
        super(container);
        instance = this;
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Commands.register(Base.class);
        Config.load();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        Config.load();
    }

    public static CmdScheduler get() {
        return instance;
    }

}