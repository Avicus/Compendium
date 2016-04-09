package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.Locale;

public class UnlocalizedString implements Localizable, LocalizableText {
    private final String text;

    public UnlocalizedString(String text) {
        this.text = text;
    }

    @Override
    public String translate(Locale locale) {
        return this.text;
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        // generate a new one every time
        return new TextComponent(this.text);
    }
}
