package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.avicus.compendium.plugin.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
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
    private final boolean decoration;
    private final TextStyle style;

    /**
     * @param date
     * @param decoration True to include localized "ago" or "from now".
     * @param style
     */
    public LocalizedTime(Date date, boolean decoration, TextStyle style) {
        this.date = date;
        this.decoration = decoration;
        this.style = style;
    }

    public LocalizedTime(Date date, boolean decoration) {
        this(date, decoration, TextStyle.create());
    }

    @Override
    public TextComponent translate(Locale locale) {
        PrettyTime pretty = new PrettyTime(locale);
        String time = pretty.format(this.date);
        Localizable result;
        if (this.decoration) {
            Date now = new Date();
            if (now.after(this.date))
                result = Messages.TIME_AGO.with(this.style, time);
            else
                result = Messages.TIME_FROM_NOW.with(this.style, time);
        }
        else {
            result = new UnlocalizedText(time, this.style);
        }

        return result.translate(locale);
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public LocalizedTime duplicate() {
        return new LocalizedTime(this.date, this.decoration, this.style.duplicate());
    }
}
