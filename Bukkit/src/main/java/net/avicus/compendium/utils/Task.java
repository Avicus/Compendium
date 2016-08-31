package net.avicus.compendium.utils;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public abstract class Task {
    @Getter private final List<BukkitTask> activeTasks = new ArrayList<>();
    @Getter private boolean running; // true if run() currently in progress

    protected Task() {

    }

    public abstract Plugin getPlugin();

    public abstract void run() throws Exception;

    public Task now() {
        BukkitRunnable runnable = newRunnable();
        runnable.runTask(getPlugin());
        return this;
    }

    public Task nowAsync() {
        BukkitRunnable runnable = newRunnable();
        this.activeTasks.add(runnable.runTaskAsynchronously(getPlugin()));
        return this;
    }

    public Task later(int ticksDelay) {
        BukkitRunnable runnable = newRunnable();

        if (!getPlugin().isEnabled())
            return null;

        this.activeTasks.add(runnable.runTaskLater(getPlugin(), ticksDelay));
        return this;
    }

    public Task laterAsync(int ticksDelay) {
        BukkitRunnable runnable = newRunnable();

        if (!getPlugin().isEnabled())
            return null;

        this.activeTasks.add(runnable.runTaskLaterAsynchronously(getPlugin(), ticksDelay));
        return this;
    }

    public Task repeat(int ticksDelay, int ticksInterval) {
        BukkitRunnable runnable = newRunnable();
        this.activeTasks.add(runnable.runTaskTimer(getPlugin(), ticksDelay, ticksInterval));
        return this;
    }

    public Task repeatAsync(int ticksDelay, int ticksInterval) {
        BukkitRunnable runnable = newRunnable();
        this.activeTasks.add(runnable.runTaskTimerAsynchronously(getPlugin(), ticksDelay, ticksInterval));
        return this;
    }

    public boolean cancel() {
        if (this.activeTasks.isEmpty())
            return false;
        this.activeTasks.remove(this.activeTasks.size() - 1).cancel();
        return true;
    }

    public void cancelAll() {
        while (this.activeTasks.size() > 0)
            cancel();
    }

    public boolean isActive() {
        return this.activeTasks.size() > 0;
    }

    private BukkitRunnable newRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                running = true;
                try {
                    Task.this.run();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                running = false;
            }
        };
    }
}