package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalizedNumber implements Localizable {
    private final Number number;
    private final TextStyle style;

    public LocalizedNumber(Number number, TextStyle style) {
        this.number = number;
        this.style = style;
    }

    public LocalizedNumber(Number number) {
        this(number, TextStyle.create());
    }

    @Override
    public TextComponent translate(Locale locale) {
        NumberFormat format = NumberFormat.getInstance(locale);
        return new UnlocalizedText(format.format(this.number), this.style).translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public Localizable duplicate() {
        return new LocalizedNumber(this.number, this.style.duplicate());
    }
}
