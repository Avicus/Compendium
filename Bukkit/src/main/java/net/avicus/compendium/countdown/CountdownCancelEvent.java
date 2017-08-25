package net.avicus.compendium.countdown;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event which is called when a {@link Countdown} is canceled.
 */
public class CountdownCancelEvent extends Event {

  private static final HandlerList handlers = new HandlerList();
  @Getter
  private final Countdown canceled;

  public CountdownCancelEvent(Countdown canceled) {
    this.canceled = canceled;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public HandlerList getHandlers() {
    return handlers;
  }
}
