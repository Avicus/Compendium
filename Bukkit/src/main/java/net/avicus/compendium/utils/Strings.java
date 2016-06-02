package net.avicus.compendium.utils;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.util.ChatPaginator;
import org.joda.time.Duration;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

public class Strings {
    public static String addColors(String message) {
        if (message == null)
            return null;
        return ChatColor.translateAlternateColorCodes('^', message);
    }

    public static String removeColors(String message) {
        return message == null ? null : ChatColor.stripColor(message);
    }

    public static String toPercent(double proportion) {
        int percent = (int) Math.floor(proportion * 100.0);
        return percent + "%";
    }

    public static ChatColor toChatColor(DyeColor color) {
        switch (color) {
            case WHITE:
                return ChatColor.WHITE;
            case ORANGE:
                return ChatColor.GOLD;
            case MAGENTA:
                return ChatColor.LIGHT_PURPLE;
            case LIGHT_BLUE:
                return ChatColor.BLUE;
            case YELLOW:
                return ChatColor.YELLOW;
            case LIME:
                return ChatColor.GREEN;
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case GRAY:
                return ChatColor.DARK_GRAY;
            case SILVER:
                return ChatColor.GRAY;
            case CYAN:
                return ChatColor.DARK_AQUA;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            case BLUE:
                return ChatColor.DARK_BLUE;
            case BROWN:
                return ChatColor.GOLD;
            case GREEN:
                return ChatColor.DARK_GREEN;
            case RED:
                return ChatColor.DARK_RED;
            case BLACK:
                return ChatColor.BLACK;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static ChatColor toChatColor(double proportion) {
        Preconditions.checkArgument(proportion >= 0 && proportion <= 1);
        if (proportion <= 0.15)
            return ChatColor.RED;
        else if (proportion <= 0.5)
            return ChatColor.YELLOW;
        else
            return ChatColor.GREEN;
    }

    private static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendDays().appendSuffix("d")
            .appendHours().appendSuffix("h")
            .appendMinutes().appendSuffix("m")
            .appendSeconds().appendSuffix("s")
            .appendSeconds()
            .toFormatter();

    public static Duration toDuration(String format) {
        String text = format.toLowerCase().replace(" ", "");

        if (text.equals("oo"))
            return Seconds.MAX_VALUE.toStandardDuration();
        else if (text.equals("-oo"))
            return Seconds.MIN_VALUE.toStandardDuration();

        return periodFormatter.parsePeriod(text).toStandardDuration();
    }

    public static String secondsToClock(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hours == 0)
            return String.format("%02d:%02d", minutes, secs);

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static <T> String join(List<T> parts, String delimeter, Stringify<T> stringify) {
        String text = "";
        for (T part : parts) {
            text += stringify.on(part);
            if (parts.indexOf(part) != parts.size() - 1)
                text += delimeter;
        }
        return text;
    }

    public static String padChatMessage(TextComponent message, String padChar, ChatColor padColor, ChatColor messageColor) {
        return padChatMessage(message.getText(), padChar, padColor, messageColor);
    }

    public static String padChatMessage(String message, String padChar, ChatColor padColor, ChatColor messageColor) {
        message = " " + message + " ";
        String pad = com.google.common.base.Strings.repeat(padChar, (ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - ChatColor.stripColor(message).length() - 2) / (padChar.length() * 2));
        return padColor + pad + ChatColor.RESET + messageColor + message + ChatColor.RESET + padColor + pad;
    }

    public interface Stringify<T> {
        String on(T object);
    }
}
