package net.avicus.compendium.settings;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a setting changes.
 */
public class SettingChangeEvent extends Event implements Cancellable {
    @Getter private final Object storeType;
    @Getter private final SettingContext value;
    @Getter @Setter private boolean cancelled;

    public SettingChangeEvent(Object storeType, SettingContext value) {
        this.storeType = storeType;
        this.value = value;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
