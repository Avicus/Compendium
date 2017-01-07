package net.avicus.compendium.countdown;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownStartEvent extends Event {
    @Getter private final Countdown started;

    public CountdownStartEvent(Countdown started) {
        this.started = started;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
