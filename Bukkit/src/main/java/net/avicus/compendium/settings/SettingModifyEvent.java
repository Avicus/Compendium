package net.avicus.compendium.settings;

import lombok.Getter;
import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a setting if modified by a player.
 */
public class SettingModifyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final CompendiumPlugin plugin;
    @Getter private final SettingContext setting;
    @Getter private final Player player;

    public SettingModifyEvent(CompendiumPlugin plugin, SettingContext setting, Player player) {
        this.plugin = plugin;
        this.setting = setting;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
