package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.text.MessageFormat;
import java.util.Locale;

public class LocalizedString implements Localizable, LocalizableText {
    private final LocaleBundle bundle;
    private final String key;
    private final Localizable[] arguments;

    public LocalizedString(LocaleBundle bundle, String key, Localizable... arguments) {
        this.bundle = bundle;
        this.key = key;
        this.arguments = arguments;
    }

    @Override
    public String translate(Locale locale) {
        String format = this.bundle.get(locale, this.key).orElse("translation missing: " + this.key);

        Object[] args = new Object[this.arguments.length];
        for (int i = 0; i < args.length; i++)
            args[i] = this.arguments[i].translate(locale);

        return MessageFormat.format(format, args);
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        return new TextComponent(translate(locale));
    }
}
