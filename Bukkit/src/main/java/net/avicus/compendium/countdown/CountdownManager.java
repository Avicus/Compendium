package net.avicus.compendium.countdown;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.Duration;

/**
 * Container class that should be used for the creation and execution of {@link Countdown}s.
 */
public class CountdownManager implements Listener {

  private final Map<Countdown, CountdownTask> countdowns = Maps.newHashMap();

  public Map<Countdown, CountdownTask> getCountdowns() {
    return this.countdowns;
  }

  /**
   * Find a countdown from a bukkit task ID.
   *
   * @param taskId to search for
   * @return a countdown that has the ID
   */
  @Nullable
  CountdownTask findByTaskId(int taskId) {
    for (CountdownTask task : this.countdowns.values()) {
      if (task.getTaskId() == taskId) {
        return task;
      }
    }
    return null;
  }

  /**
   * Remove a countdown from the manager.
   *
   * @param countdown to remove
   */
  void remove(Countdown countdown) {
    this.countdowns.remove(countdown);
  }

  /**
   * Called when a player quits to remove all active bars tied to a player.
   */
  @EventHandler
  public void quit(final PlayerQuitEvent event) {
    final UUID uniqueId = event.getPlayer().getUniqueId();
    for (Countdown countdown : this.countdowns.keySet()) {
      countdown.bars.remove(uniqueId);
    }
  }

  /**
   * Start a countdown and add it to the manager.
   *
   * @param countdown to start
   */
  public void start(Countdown countdown) {
    CountdownTask task = new CountdownTask(this, countdown);
    this.countdowns.put(countdown, task);
    countdown.onStart();
    task.runTaskTimer(CompendiumPlugin.getInstance(), 0, 20);

    CompendiumPlugin.getInstance().getServer().getPluginManager()
        .callEvent(new CountdownStartEvent(countdown));
  }

  /**
   * Cancel a countdown.
   *
   * @param countdown to cancel
   */
  public void cancel(Countdown countdown) {
    @Nullable CountdownTask task = this.countdowns.remove(countdown);
    if (task != null) {
      task.cancel();
      CompendiumPlugin.getInstance().getServer().getPluginManager()
          .callEvent(new CountdownCancelEvent(countdown));
    }
  }

  /**
   * Cancel all countdowns that match the supplied {@link Predicate}.
   *
   * @param predicate to run all countdowns against
   */
  public void cancelAll(Predicate<Countdown> predicate) {
    Set<Countdown> cancelled = Sets.newHashSet();
    for (Countdown countdown : Sets.newHashSet(this.countdowns.keySet())) {
      if (predicate.test(countdown)) {
        this.cancel(countdown);
        cancelled.add(countdown);
      }
    }

    this.countdowns.keySet().removeAll(cancelled);
    cancelled.forEach((countdown -> CompendiumPlugin.getInstance().getServer().getPluginManager()
        .callEvent(new CountdownCancelEvent(countdown))));
  }

  /**
   * Cancel all countdowns registered with the manager and clear the manager.
   */
  public void cancelAll() {
    Iterator<CountdownTask> it = this.countdowns.values().iterator();
    while (it.hasNext()) {
      it.next().cancel();
      it.remove();
    }

    this.countdowns.keySet().forEach(
        (countdown -> CompendiumPlugin.getInstance().getServer().getPluginManager()
            .callEvent(new CountdownCancelEvent(countdown))));
    this.countdowns.clear();
  }

  /**
   * Test if a countdown of the specified class is running.
   *
   * @param clazz to check against
   * @return if a countdown of the specified class is running
   */
  public boolean isRunning(Class<? extends Countdown> clazz) {
    return this.countdowns.keySet().stream().anyMatch(clazz::isInstance);
  }

  /**
   * Get the time remaining of a countdown.
   *
   * @param countdown to get time remaining for
   * @return the amount of time remaining
   */
  @Nullable
  public Duration getTimeRemaining(Countdown countdown) {
    if (!this.countdowns.containsKey(countdown)) {
      return null;
    }

    CountdownTask task = this.countdowns.get(countdown);

    return Duration.standardSeconds(
        task.getCountdown().getDuration().toStandardSeconds().getSeconds() - task
            .getElapsedSeconds());
  }
}
