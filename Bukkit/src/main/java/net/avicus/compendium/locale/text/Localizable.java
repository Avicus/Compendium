package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Locale;

public interface Localizable {
    TextComponent translate(Locale locale);

    TextStyle style();

    Localizable duplicate();
}
