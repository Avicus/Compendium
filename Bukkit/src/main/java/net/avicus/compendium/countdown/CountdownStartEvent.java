package net.avicus.compendium.countdown;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event which is called when a {@link Countdown} is started.
 */
public class CountdownStartEvent extends Event {

  private static final HandlerList handlers = new HandlerList();
  @Getter
  private final Countdown started;

  public CountdownStartEvent(Countdown started) {
    this.started = started;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public HandlerList getHandlers() {
    return handlers;
  }
}
