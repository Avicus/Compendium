package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.LocaleBundle;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A {@link LocalizedText} that can have a translation inside of it to allow for double translation.
 */
public class LocalizedTextFormat implements Localizable {
    private final LocaleBundle bundle;
    private final Localizable format;
    private final List<Localizable> arguments;
    private final TextStyle style;

    public LocalizedTextFormat(LocaleBundle bundle, Localizable format, Localizable... arguments) {
        this(bundle, format, TextStyle.create(), arguments.length == 0 ? Collections.emptyList() : Arrays.asList(arguments));
    }

    public LocalizedTextFormat(LocaleBundle bundle, Localizable format, TextStyle style, Localizable... arguments) {
        this(bundle, format, style, arguments.length == 0 ? Collections.emptyList() : new ArrayList<>(Arrays.asList(arguments)));
    }

    public LocalizedTextFormat(LocaleBundle bundle, Localizable format, List<Localizable> arguments) {
        this(bundle, format, TextStyle.create(), arguments);
    }

    public LocalizedTextFormat(LocaleBundle bundle, Localizable format, TextStyle style, List<Localizable> arguments) {
        this.bundle = bundle;
        this.format = format;
        this.style = style;
        this.arguments = arguments;
    }

    @Override
    public BaseComponent translate(Locale locale) {
        String text = this.format.translate(locale).toLegacyText();
        UnlocalizedText sneaky = new UnlocalizedText(text, this.style, this.arguments);
        sneaky.style().inherit(this.style);

        return sneaky.translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public LocalizedTextFormat duplicate() {
        List<Localizable> arguments = this.arguments.stream()
                .map(Localizable::duplicate)
                .collect(Collectors.toList());

        return new LocalizedTextFormat(this.bundle, this.format, this.style.duplicate(), arguments);
    }
}
