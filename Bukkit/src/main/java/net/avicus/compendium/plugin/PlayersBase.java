package net.avicus.compendium.plugin;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.utils.Strings;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Optional;

public class PlayersBase {
    public static Locale getLocale(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender)
            return CompendiumPlugin.getLocaleBundle().getDefaultLocale().get();

        String full = ((Player) sender).spigot().getLocale();
        return new Locale(full.split("_")[0], full.split("_")[1]);
    }

    public static void message(CommandSender sender, TextComponent text) {
        message(sender, text, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static void message(CommandSender sender, TextComponent text, Optional<String> padChar, Optional<ChatColor> messageColor, Optional<ChatColor> padColor) {
        if (padChar.isPresent())
            text = Strings.padChatComponent(text, padChar.get(), padColor.get(), messageColor.get());

        if (sender instanceof ConsoleCommandSender)
            sender.sendMessage(text.toLegacyText());
        else if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(text);
    }

    public static void message(CommandSender sender, Localizable... messages) {
        message(sender, Optional.empty(), Optional.empty(), Optional.empty(), messages);
    }

    public static void message(CommandSender sender, Optional<String> padChar, Optional<ChatColor> messageColor, Optional<ChatColor> padColor, Localizable... messages) {
        Locale locale = getLocale(sender);

        TextComponent message = new TextComponent(messages[0].translate(locale));
        for (int i = 1; i < messages.length; i++)
            message.addExtra(messages[i].translate(locale));

        if (padChar.isPresent())
            message = Strings.padChatComponent(message, padChar.get(), padColor.get(), messageColor.get());

        if (sender instanceof ConsoleCommandSender)
            sender.sendMessage(message.toLegacyText());
        else if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(message);
    }

    public static void broadcast(Optional<String> padChar, Optional<ChatColor> messageColor, Optional<ChatColor> padColor, Localizable... messages) {
        for (Player player : Bukkit.getOnlinePlayers())
            message(player, padChar, messageColor, padColor, messages);
        console(messages);
    }

    public static void broadcast(Localizable... messages) {
        broadcast(Optional.empty(), Optional.empty(), Optional.empty(), messages);
    }

    public static void console(Localizable... messages) {
        message(Bukkit.getConsoleSender(), messages);
    }

    @Deprecated
    public static void console(String message) {
        message(Bukkit.getConsoleSender(), message);
    }

    @Deprecated
    public static void message(CommandSender sender, String message) {
        sender.sendMessage(message);
    }
}
