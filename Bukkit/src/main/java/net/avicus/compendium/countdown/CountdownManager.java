package net.avicus.compendium.countdown;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.Duration;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class CountdownManager implements Listener {

    private final Map<Countdown, CountdownTask> countdowns = Maps.newHashMap();

    public Map<Countdown, CountdownTask> getCountdowns() {
        return this.countdowns;
    }

    @Nullable
    CountdownTask findByTaskId(int taskId) {
        for (CountdownTask task : this.countdowns.values()) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    void remove(Countdown countdown) {
        this.countdowns.remove(countdown);
    }

    @EventHandler
    public void quit(final PlayerQuitEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();
        for (Countdown countdown : this.countdowns.keySet()) {
            countdown.bars.remove(uniqueId);
        }
    }

    public void start(Countdown countdown) {
        CountdownTask task = new CountdownTask(this, countdown);
        this.countdowns.put(countdown, task);
        countdown.onStart();
        task.runTaskTimer(CompendiumPlugin.getInstance(), 0, 20);

        CompendiumPlugin.getInstance().getServer().getPluginManager().callEvent(new CountdownStartEvent(countdown));
    }

    public void cancel(Countdown countdown) {
        @Nullable CountdownTask task = this.countdowns.remove(countdown);
        if (task != null) {
            task.cancel();
            CompendiumPlugin.getInstance().getServer().getPluginManager().callEvent(new CountdownCancelEvent(countdown));
        }
    }

    public void cancelAll(Predicate<Countdown> predicate) {
        Set<Countdown> cancelled = Sets.newHashSet();
        for (Countdown countdown : this.countdowns.keySet()) {
            if (predicate.test(countdown)) {
                this.cancel(countdown);
                cancelled.add(countdown);
            }
        }

        this.countdowns.keySet().removeAll(cancelled);
        cancelled.forEach((countdown -> CompendiumPlugin.getInstance().getServer().getPluginManager().callEvent(new CountdownCancelEvent(countdown))));
    }

    public void cancelAll() {
        Iterator<CountdownTask> it = this.countdowns.values().iterator();
        while (it.hasNext()) {
            it.next().cancel();
            it.remove();
        }

        this.countdowns.keySet().forEach((countdown -> CompendiumPlugin.getInstance().getServer().getPluginManager().callEvent(new CountdownCancelEvent(countdown))));
        this.countdowns.clear();
    }

    public boolean isRunning(Class<? extends Countdown> clazz) {
        return this.countdowns.values().stream().anyMatch(countdown -> clazz.isInstance(clazz));
    }

    @Nullable
    public Duration getTimeRemaining(Countdown countdown) {
        if (!this.countdowns.containsKey(countdown))
            return null;

        CountdownTask task = this.countdowns.get(countdown);

        return Duration.standardSeconds(task.getCountdown().getDuration().toStandardSeconds().getSeconds() - task.getElapsedSeconds());
    }
}
