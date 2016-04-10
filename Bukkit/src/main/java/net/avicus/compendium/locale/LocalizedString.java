package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocalizedString implements Localizable, LocalizableText {
    private final LocaleBundle bundle;
    private final String key;
    private final List<Localizable> arguments;

    public LocalizedString(LocaleBundle bundle, String key, Localizable... arguments) {
        this.bundle = bundle;
        this.key = key;
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
    }

    @Override
    public String translate(Locale locale) {
        String format = this.bundle.get(locale, this.key).orElse("translation missing: " + this.key);
        UnlocalizedString string = new UnlocalizedString(format, this.arguments);
        return string.translate(locale);
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        return new TextComponent(translate(locale));
    }
}
