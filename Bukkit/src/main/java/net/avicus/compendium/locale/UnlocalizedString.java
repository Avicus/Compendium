package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UnlocalizedString implements Localizable, LocalizableText {
    private final String format;
    private final List<Localizable> arguments;

    public UnlocalizedString(String format, Localizable... arguments) {
        this(format, new ArrayList<>(Arrays.asList(arguments)));
    }

    public UnlocalizedString(String format, List<Localizable> arguments) {
        this.format = format;
        this.arguments = arguments;
    }

    @Override
    public String translate(Locale locale) {
        Object[] args = new Object[this.arguments.size()];
        for (int i = 0; i < args.length; i++)
            args[i] = this.arguments.get(i).translate(locale);

        return MessageFormat.format(this.format, args);
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        // generate a new one every time
        return new TextComponent(this.format);
    }
}
