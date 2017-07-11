package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.avicus.compendium.Time;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Date;
import java.util.Locale;

/**
 * A date object that can be translated to any locale with a specific style in the _ago format.
 *
 * EXAMPLES:
 * 5 seconds ago,
 * 1 day from now,
 * moments ago,
 * etc.
 */
public class LocalizedTime implements Localizable {
    private final Date date;
    private final TextStyle style;

    public LocalizedTime(Date date, TextStyle style) {
        this.date = date;
        this.style = style;
    }

    public LocalizedTime(Date date) {
        this(date, TextStyle.create());
    }

    @Override
    public BaseComponent translate(Locale locale) {
        String time = Time.prettyTime(locale).format(this.date);
        return new UnlocalizedText(time, this.style).translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public LocalizedTime duplicate() {
        return new LocalizedTime(this.date, this.style.duplicate());
    }
}
