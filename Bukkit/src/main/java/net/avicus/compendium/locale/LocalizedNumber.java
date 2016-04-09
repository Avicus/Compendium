package net.avicus.compendium.locale;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalizedNumber implements Localizable {
    private final Number number;

    public LocalizedNumber(Number number) {
        this.number = number;
    }

    // todo: add rounding/number of decimals

    @Override
    public String translate(Locale locale) {
        return NumberFormat.getInstance(locale).format(this.number);
    }
}
