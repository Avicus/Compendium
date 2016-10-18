package net.avicus.compendium.plugin.countdown;

import lombok.Getter;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.plugin.CompendiumPlugin;
import net.avicus.compendium.plugin.CompendiumTask;
import net.avicus.compendium.plugin.PlayersBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.joda.time.Duration;
import org.joda.time.Seconds;

public abstract class Countdown extends CompendiumTask {
    @Getter private final Duration duration;
    private int elapsedSeconds;

    public Countdown(Duration duration) {
        super();
        this.duration = duration;
        this.elapsedSeconds = 0;
    }

    public void setElapsedSeconds(int seconds) {
        this.elapsedSeconds = seconds;
    }

    public Countdown start() {
        if (this.isRunning())
            return this;
        this.repeat(0, 20);
        return this;
    }

    @Override
    public void run() {
        if (this.duration.getStandardSeconds() - this.elapsedSeconds <= 0) {
            end();
            cancel();
            return;
        }

        Duration elapsed = Seconds.seconds(this.elapsedSeconds).toStandardDuration();
        Duration remaining = this.duration.minus(elapsed);

        run(elapsed, remaining);
        this.elapsedSeconds++;
    }

    protected void updateBossBar(Localizable text, Duration elapsed) {
        float percent = (float) 1 - ((float) elapsed.getStandardSeconds() / (float) this.getDuration().getStandardSeconds());
        for (Player player : Bukkit.getOnlinePlayers())
            CompendiumPlugin.getBossy().set(player, text.translate(PlayersBase.getLocale(player)).toLegacyText(), percent);
    }

    protected void removeBar() {
        for (Player player : Bukkit.getOnlinePlayers())
            CompendiumPlugin.getBossy().hide(player);
    }

    /**
     * Called in one second intervals.
     * @param elapsedTime The amount of time elapsed.
     * @param remainingTime The amount of time remaining in the countdown.
     */
    public abstract void run(Duration elapsedTime, Duration remainingTime);

    /**
     * Called when the countdown is complete (at 0 seconds remaining).
     */
    public abstract void end();
}
