package com.mcsimonflash.sponge.cmdscheduler;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.cmdscheduler.command.Base;
import com.mcsimonflash.sponge.cmdscheduler.internal.Config;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(id = "cmdscheduler", name = "CmdScheduler", version = "1.1.1", dependencies = @Dependency(id = "cmdcontrol"), url = "https://ore.spongepowered.org/Simon_Flash/CmdScheduler", authors = "Simon_Flash")
public class CmdScheduler extends CmdPlugin {

    private static CmdScheduler instance;

    @Inject
    public CmdScheduler(PluginContainer container) {
        super(container);
        instance = this;
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        getCommands().register(Base.class);
        Config.load();
    }

    @Listener
    public void onStart(GameStartedServerEvent event) {
        Config.tasks.values().forEach(t -> t.getTask().start(getContainer()));
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        getMessages().reload();
        Config.load();
        Config.tasks.values().forEach(t -> t.getTask().start(getContainer()));
    }

    public static CmdScheduler get() {
        return instance;
    }

    public static Text getMessage(CommandSource src, String key, Object... args) {
        return get().getPrefix().concat(get().getMessages().get(key, src.getLocale()).args(args).toText());
    }

}