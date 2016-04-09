package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.Locale;

public interface LocalizableText {
    TextComponent toComponent(Locale locale);
}
