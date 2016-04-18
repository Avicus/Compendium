package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.Locale;

public interface Localizable {
    /**
     * Translates this to a language.
     * @param locale
     * @return
     */
    TextComponent translate(Locale locale);

    /**
     * Get the style of this.
     * @return
     */
    TextStyle style();

    /**
     * Copy this and its styles.
     * @return
     */
    Localizable duplicate();

    /**
     * Helper method to set the color of this.
     * @param color
     * @return
     */
    Localizable color(ChatColor color);
}
