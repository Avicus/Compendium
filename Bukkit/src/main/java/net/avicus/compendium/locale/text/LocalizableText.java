package net.avicus.compendium.locale.text;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.Locale;

public abstract class LocalizableText<T extends LocalizableText> extends TextStyle<T> {
    public String toPlainText(Locale locale) {
        return toComponent(locale).toPlainText();
    }

    public String toLegacyText(Locale locale) {
        return toComponent(locale).toLegacyText();
    }

    public abstract TextComponent toComponent(Locale locale);

    public abstract T duplicate();
}
