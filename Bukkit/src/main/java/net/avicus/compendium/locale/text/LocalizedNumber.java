package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalizedNumber implements Localizable {
    private final Number number;
    private final int minDecimals;
    private final int maxDecimals;
    private final TextStyle style;

    public LocalizedNumber(Number number, int minDecimals, int maxDecimals, TextStyle style) {
        this.number = number;
        this.minDecimals = minDecimals;
        this.maxDecimals = maxDecimals;
        this.style = style;
    }

    public LocalizedNumber(Number number, int minDecimals, int maxDecimals) {
        this(number, minDecimals, maxDecimals, TextStyle.create());
    }

    public LocalizedNumber(Number number, TextStyle style) {
        this(number, 0, 3, style);
    }

    public LocalizedNumber(Number number) {
        this(number, TextStyle.create());
    }


    @Override
    public BaseComponent translate(Locale locale) {
        NumberFormat format = NumberFormat.getInstance(locale);
        format.setMinimumFractionDigits(this.minDecimals);
        format.setMaximumFractionDigits(this.maxDecimals);

        return new UnlocalizedText(format.format(this.number), this.style).translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public Localizable duplicate() {
        return new LocalizedNumber(this.number, this.minDecimals, this.maxDecimals, this.style.duplicate());
    }
}
