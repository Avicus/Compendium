package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.TimeUnit;
import org.ocpsoft.prettytime.units.JustNow;

import java.util.Date;
import java.util.Locale;

/**
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
    public TextComponent translate(Locale locale) {
        PrettyTime pretty = new PrettyTime(locale);
        // "moments ago" only shows when less than 5 seconds
        pretty.getUnits()
              .stream()
              .filter(unit -> unit instanceof JustNow)
              .forEach(unit -> ((JustNow) unit).setMaxQuantity(10000L));
        String time = pretty.format(this.date);
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
