package net.avicus.compendium.locale;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class Locales {
    public static Locale getLocale(CommandSender sender) {
        if (sender instanceof Player)
            return Locale.forLanguageTag(((Player) sender).spigot().getLocale().replace("_", "-"));
        // Todo: config.yml default?
        return Locale.ENGLISH;
    }
}
