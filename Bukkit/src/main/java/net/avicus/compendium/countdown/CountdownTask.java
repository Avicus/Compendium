package net.avicus.compendium.countdown;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import org.joda.time.Duration;
import org.joda.time.Seconds;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CountdownTask extends BukkitRunnable {

    private final CountdownManager manager;
    @Getter private final Countdown countdown;
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private int elapsedSeconds;

    @Override
    public void run() {
        if (this.countdown.getDuration().getStandardSeconds() - this.elapsedSeconds <= 0) {
            this.countdown.onEnd();
            this.cancel();
            this.manager.remove(this.countdown);
            return;
        }

        Duration elapsed = Seconds.seconds(this.elapsedSeconds).toStandardDuration();
        Duration remaining = this.countdown.getDuration().minus(elapsed);

        this.countdown.onTick(elapsed, remaining);
        if (this.countdown.resetPending()) {
            this.elapsedSeconds = 0;
        } else {
            this.elapsedSeconds++;
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.countdown.onCancel();
    }
}
