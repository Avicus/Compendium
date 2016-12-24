package net.avicus.compendium.countdown;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.joda.time.Duration;
import org.joda.time.Seconds;

public class RestartingCountdown extends Countdown {

    public RestartingCountdown() {
        super(Seconds.seconds(15).toStandardDuration());
    }

    public RestartingCountdown(Duration duration) {
        super(duration);
    }

    @Override
    public Localizable getName() {
        return Messages.GENERIC_COUNTDOWN_TYPE_RESTARTING_NAME.with();
    }

    @Override
    protected void onTick(Duration elapsedTime, Duration remainingTime) {
        int sec = (int) remainingTime.getStandardSeconds();
        if (sec % 5 == 0 || sec <= 5) {
            UnlocalizedText time = new UnlocalizedText(sec + "", ChatColor.RED);

            LocalizedFormat formatter = Messages.GENERIC_COUNTDOWN_TYPE_RESTARTING_TIME_PLURAL;
            if (sec == 1)
                formatter = Messages.GENERIC_COUNTDOWN_TYPE_RESTARTING_TIME_SINGULAR;

            LocalizedText message = formatter.with(ChatColor.GOLD, time);
            Bukkit.broadcast(message);
        }
    }

    @Override
    protected void onEnd() {
        Bukkit.shutdown();
    }
}
