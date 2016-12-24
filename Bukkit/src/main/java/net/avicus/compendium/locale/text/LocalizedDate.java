package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalizedDate implements Localizable {
    private final Date date;
    private final TextStyle style;

    public LocalizedDate(Date date, TextStyle style) {
        this.date = date;
        this.style = style;
    }

    public LocalizedDate(Date date) {
        this(date, TextStyle.create());
    }

    @Override
    public BaseComponent translate(Locale locale) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        return new UnlocalizedText(format.format(this.date), this.style).translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public LocalizedDate duplicate() {
        return new LocalizedDate(this.date, this.style.duplicate());
    }
}
