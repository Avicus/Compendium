package net.avicus.compendium.plugin;

import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class Players {
    public static void message(CommandSender sender, TextComponent text) {
        if (sender instanceof ConsoleCommandSender)
            sender.sendMessage(text.toLegacyText());
        else if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(text);
    }

    public static void message(CommandSender sender, Localizable... messages) {
        Locale locale = Locales.getLocale(sender);
        TextComponent message = new TextComponent(messages[0].translate(locale));
        for (int i = 1; i < messages.length; i++)
            message.addExtra(messages[i].translate(locale));


        if (sender instanceof ConsoleCommandSender)
            sender.sendMessage(message.toLegacyText());
        else if (sender instanceof Player)
            ((Player) sender).spigot().sendMessage(message);
    }

    public static void broadcast(Localizable... messages) {
        for (Player player : Bukkit.getOnlinePlayers())
            message(player, messages);
        console(messages);
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
