package net.avicus.compendium.countdown;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.avicus.compendium.plugin.CompendiumPlugin;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

public class CountdownManager {

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

    public void start(Countdown countdown) {
        CountdownTask task = new CountdownTask(this, countdown);
        this.countdowns.put(countdown, task);
        countdown.onStart();
        task.runTaskTimer(CompendiumPlugin.getInstance(), 0, 20);
    }

    public void cancel(Countdown countdown) {
        @Nullable CountdownTask task = this.countdowns.remove(countdown);
        if (task != null) {
            task.cancel();
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
    }

    public void cancelAll() {
        Iterator<CountdownTask> it = this.countdowns.values().iterator();
        while (it.hasNext()) {
            it.next().cancel();
            it.remove();
        }

        this.countdowns.clear();
    }

    public boolean isRunning(Class<? extends Countdown> clazz) {
        return this.countdowns.values().stream().anyMatch(countdown -> clazz.isInstance(clazz));
    }
}
