package net.avicus.compendium.locale.text;

import net.avicus.compendium.locale.LocalizableText;
import net.avicus.compendium.locale.LocalizedString;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocalizedText extends TextStyle<LocalizedText> implements LocalizableText {
    private final LocalizedString base;
    private final List<LocalizableText> arguments;
    private final List<LocalizableText> extras;

    public LocalizedText(LocalizedString base, LocalizableText... arguments) {
        this.base = base;
        this.arguments = new ArrayList<>(Arrays.asList(arguments));
        this.extras = new ArrayList<>();
    }

    public void addExtra(LocalizableText extra) {
        this.extras.add(extra);
    }

    public TextComponent toComponent(Locale locale) {
        String format = this.base.translate(locale);
        UnlocalizedText text = new UnlocalizedText(format, this.arguments, this.extras);
        text.inherit(this);
        return text.toComponent(locale);
    }
}
