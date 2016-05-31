package net.avicus.compendium.utils;

import com.google.common.base.Strings;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

public class ChatUtils {

    public static String dashedChatMessage(TextComponent message, String c, ChatColor dashColor, ChatColor messageColor) {
        return dashedChatMessage(message.getText(), c, dashColor, messageColor);
    }

    public static String dashedChatMessage(String message, String c, ChatColor dashColor, ChatColor messageColor) {
        message = " " + message + " ";
        String dashes = Strings.repeat(c, (ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - ChatColor.stripColor(message).length() - 2) / (c.length() * 2));
        return dashColor + dashes + ChatColor.RESET + messageColor + message + ChatColor.RESET + dashColor + dashes;
    }
}
