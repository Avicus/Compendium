package net.avicus.compendium.plugin.countdown;


import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayersBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.joda.time.Duration;
import org.joda.time.Seconds;

public class RestartingCountdown extends Countdown {
    public RestartingCountdown(Duration duration) {
        super(duration);
    }

    public RestartingCountdown() {
        super(Seconds.seconds(15).toStandardDuration());
    }

    @Override
    public void run(Duration elapsedTime, Duration remainingTime) {
        int sec = (int) remainingTime.getStandardSeconds();
        if (sec % 5 == 0 || sec <= 5) {
            UnlocalizedText time = new UnlocalizedText(sec + "", ChatColor.RED);

            LocalizedFormat formatter = Messages.GENERIC_RESTARTING_PLURAL;
            if (sec == 1)
                formatter = Messages.GENERIC_RESTARTING;

            LocalizedText message = formatter.with(ChatColor.GOLD, time);
            PlayersBase.broadcast(message);
        }
    }

    @Override
    public void end() {
        Bukkit.shutdown();
    }
}
