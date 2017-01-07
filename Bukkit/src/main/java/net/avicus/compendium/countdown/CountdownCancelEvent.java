package net.avicus.compendium.countdown;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownCancelEvent extends Event {
    @Getter private final Countdown canceled;

    public CountdownCancelEvent(Countdown canceled) {
        this.canceled = canceled;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
