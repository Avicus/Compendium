package net.avicus.compendium.locale;

import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalizedNumber implements Localizable {
    @Getter private final Number number;
    @Getter @Setter int minimumDecimals = -1;
    @Getter @Setter int maximumDecimals = 5;

    public LocalizedNumber(Number number) {
        this.number = number;
    }

    @Override
    public String translate(Locale locale) {
        NumberFormat formatter = NumberFormat.getInstance(locale);
        formatter.setMinimumFractionDigits(this.minimumDecimals);
        formatter.setMaximumFractionDigits(this.maximumDecimals);
        return formatter.format(this.number);
    }
}
