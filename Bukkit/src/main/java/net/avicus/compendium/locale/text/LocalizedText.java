package net.avicus.compendium.locale.text;

import net.avicus.compendium.locale.LocaleBundle;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocalizedText extends LocalizableText<LocalizedText> {
    private final LocaleBundle bundle;
    private final String key;
    private final List<LocalizableText> arguments;
    private final List<LocalizableText> extras;

    public LocalizedText(LocaleBundle bundle, String key, LocalizableText... arguments) {
        this(bundle, key, new ArrayList<>(Arrays.asList(arguments)));
    }

    public LocalizedText(LocaleBundle bundle, String key, List<LocalizableText> arguments) {
        this(bundle, key, arguments, new ArrayList<>());
    }

    public LocalizedText(LocaleBundle bundle, String key, List<LocalizableText> arguments, List<LocalizableText> extras) {
        this.bundle = bundle;
        this.key = key;
        this.arguments = arguments;
        this.extras = extras;
    }

    public void addExtra(LocalizableText extra) {
        this.extras.add(extra);
    }

    public TextComponent toComponent(Locale locale) {
        String format = this.bundle.get(locale, this.key).orElse("missing translation: " + this.key);
        UnlocalizedText text = new UnlocalizedText(format, this.arguments, this.extras);
        text.inherit(this);
        return text.toComponent(locale);
    }

    @Override
    public LocalizedText duplicate() {
        List<LocalizableText> arguments = new ArrayList<>();
        List<LocalizableText> extras = new ArrayList<>();
        for (LocalizableText arg : this.arguments)
            arguments.add(arg.duplicate());
        for (LocalizableText extra : this.extras)
            extras.add(extra.duplicate());

        LocalizedText text = new LocalizedText(this.bundle, this.key, arguments, extras);
        text.inherit(this);
        return text;
    }
}
