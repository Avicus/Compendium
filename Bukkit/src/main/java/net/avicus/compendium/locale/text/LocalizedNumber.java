package net.avicus.compendium.locale.text;

import net.md_5.bungee.api.chat.TextComponent;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalizedNumber extends LocalizableText<LocalizedNumber> {
    private final Number number;
    private final int minimumDecimals;
    private final int maximumDecimals;

    public LocalizedNumber(Number number) {
        this(number, -1, 5);
    }

    public LocalizedNumber(Number number, int minimumDecimals, int maximumDecimals) {
        this.number = number;
        this.minimumDecimals = minimumDecimals;
        this.maximumDecimals = maximumDecimals;
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        NumberFormat formatter = NumberFormat.getInstance(locale);
        formatter.setMinimumFractionDigits(this.minimumDecimals);
        formatter.setMaximumFractionDigits(this.maximumDecimals);
        String result = formatter.format(this.number);
        return super.toComponent(result);
    }

    @Override
    public LocalizedNumber duplicate() {
        LocalizedNumber dup = new LocalizedNumber(this.number, this.minimumDecimals, this.maximumDecimals);
        dup.inherit(this);
        return dup;
    }
}
