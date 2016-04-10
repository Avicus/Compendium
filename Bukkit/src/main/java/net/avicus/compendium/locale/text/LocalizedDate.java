package net.avicus.compendium.locale.text;

import net.md_5.bungee.api.chat.TextComponent;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalizedDate extends LocalizableText<LocalizedDate> {
    private final Date date;
    private final Format format;
    private final Type type;

    public LocalizedDate(Date date) {
        this(date, Type.DATE);
    }

    public LocalizedDate(Date date, Type type) {
        this(date, type, Format.FULL);
    }

    public LocalizedDate(Date date, Type type, Format format) {
        this.date = date;
        this.type = type;
        this.format = format;
    }

    @Override
    public TextComponent toComponent(Locale locale) {
        int num = 0;

        switch (this.format) {
            case FULL:
                num = DateFormat.FULL;
                break;
            case LONG:
                num = DateFormat.LONG;
                break;
            case MEDIUM:
                num = DateFormat.MEDIUM;
                break;
            case SHORT:
                num = DateFormat.SHORT;
                break;
        }

        DateFormat format;

        if (this.type == Type.DATE)
            format = DateFormat.getDateInstance(num, locale);
        else
            format = DateFormat.getTimeInstance(num, locale);

        return toComponent(format.format(this.date));
    }

    @Override
    public LocalizedDate duplicate() {
        LocalizedDate dup = new LocalizedDate(this.date, this.type, this.format);
        dup.inherit(this);
        return dup;
    }

    public enum Type {
        DATE,
        TIME
    }

    public enum Format {
        FULL,
        LONG,
        MEDIUM,
        SHORT
    }
}
