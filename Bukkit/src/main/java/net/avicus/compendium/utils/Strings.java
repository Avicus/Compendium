package net.avicus.compendium.utils;

import com.google.common.base.Preconditions;
import net.avicus.compendium.text.Components;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;
import org.joda.time.Duration;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

// TODO - move methods into StringUtil if they work with strings, or their own class - this one is a mess
public class Strings {
    public static String addColors(String message) {
        if (message == null)
            return null;
        return ChatColor.translateAlternateColorCodes('^', message);
    }

    public static String removeColors(String message) {
        return message == null ? null : ChatColor.stripColor(message);
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
                return ChatColor.BLUE;
            case BROWN:
                return ChatColor.GOLD;
            case GREEN:
                return ChatColor.DARK_GREEN;
            case RED:
                return ChatColor.DARK_RED;
            case BLACK:
                return ChatColor.GRAY;
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

    public static String padChatMessage(BaseComponent message, String padChar, ChatColor padColor, ChatColor messageColor) {
        return padChatMessage(message.toPlainText(), padChar, padColor, messageColor);
    }

    public static BaseComponent padChatComponent(BaseComponent message, String padChar, ChatColor padColor, ChatColor messageColor) {
        return new TextComponent(padChatMessage(message.toPlainText(), padChar, padColor, messageColor));
    }

    public static BaseComponent padTextComponent(TextComponent message, String padChar, ChatColor padColor, ChatColor messageColor) {
        final String pad = paddingFor(message.toPlainText(), padChar);
        final TextComponent component = new TextComponent(padColor + pad + ChatColor.RESET);
        BaseComponent copy = Components.copyStyle(message, new TextComponent(' ' + message.toPlainText() + ' '));
        copy.setColor(messageColor.asBungee());
        component.addExtra(copy);
        component.addExtra(ChatColor.RESET.toString() + padColor + pad);
        return component;
    }

    public static String padChatMessage(String message, String padChar, ChatColor padColor, ChatColor messageColor) {
        message = " " + message + " ";
        String pad = paddingFor(message, padChar);
        return padColor + pad + ChatColor.RESET + messageColor + message + ChatColor.RESET + padColor + pad;
    }

    private static String paddingFor(String text, String padChar) {
        return com.google.common.base.Strings.repeat(padChar, (ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - ChatColor.stripColor(text).length() - 2) / (padChar.length() * 2));
    }

    /**
     * Wrap a long {@link ItemMeta} lore line and append it to a list of lore with
     * last colors copied over.
     *
     * @param lore the result list
     * @param length the length to wrap on
     * @param string the string to wrap
     */
    public static void wrapLoreCopyColors(final List<String> lore, final int length, final String string) {
        String previous = "";
        for (String wrapped : WordUtils.wrap(string, length).split(SystemUtils.LINE_SEPARATOR)) {
            if (!previous.isEmpty()) {
                previous = ChatColor.getLastColors(previous);
            }

            lore.add(previous + wrapped);
            previous = wrapped;
        }
    }

}
