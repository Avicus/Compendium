package net.avicus.compendium;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public abstract class Task {
    private static Plugin plugin;

    static {
        plugin = Bukkit.getPluginManager().getPlugin("Compendium");
    }

    @Getter private final List<BukkitTask> runnables = new ArrayList<>();
    @Getter private boolean running; // true if run() currently in progress

    public Task now() {
        BukkitRunnable runnable = newRunnable();
        runnable.runTask(plugin);
        return this;
    }

    public Task nowAsync() {
        BukkitRunnable runnable = newRunnable();
        this.runnables.add(runnable.runTaskAsynchronously(plugin));
        return this;
    }

    public Task later(int ticksDelay) {
        BukkitRunnable runnable = newRunnable();

        if (!plugin.isEnabled())
            return null;

        this.runnables.add(runnable.runTaskLater(plugin, ticksDelay));
        return this;
    }

    public Task laterAsync(int ticksDelay) {
        BukkitRunnable runnable = newRunnable();

        if (!plugin.isEnabled())
            return null;

        this.runnables.add(runnable.runTaskLaterAsynchronously(plugin, ticksDelay));
        return this;
    }

    public Task repeat(int ticksDelay, int ticksInterval) {
        BukkitRunnable runnable = newRunnable();
        this.runnables.add(runnable.runTaskTimer(plugin, ticksDelay, ticksInterval));
        return this;
    }

    public Task repeatAsync(int ticksDelay, int ticksInterval) {
        BukkitRunnable runnable = newRunnable();
        this.runnables.add(runnable.runTaskTimerAsynchronously(plugin, ticksDelay, ticksInterval));
        return this;
    }

    public boolean cancel() {
        if (this.runnables.isEmpty())
            return false;
        this.runnables.remove(this.runnables.size() - 1).cancel();
        return true;
    }

    public void cancelAll() {
        while (this.runnables.size() > 0)
            cancel();
    }

    public boolean isActive() {
        return this.runnables.size() > 0;
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

    public abstract void run() throws Exception;
}