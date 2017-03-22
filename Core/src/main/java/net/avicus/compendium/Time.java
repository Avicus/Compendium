package net.avicus.compendium;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.TimeUnit;
import org.ocpsoft.prettytime.impl.ResourcesTimeFormat;
import org.ocpsoft.prettytime.units.JustNow;

import java.util.Locale;

public final class Time {

    private Time() {
    }

    public static PrettyTime prettyTime(Locale locale) {
        final PrettyTime pretty = new PrettyTime(locale);
        // "moments ago" only shows when less than 5 seconds
        pretty.getUnits()
                .stream()
                .filter(unit -> unit instanceof JustNow)
                .forEach(unit -> ((JustNow) unit).setMaxQuantity(10000L));
        return pretty;
    }

    public static PrettyTime removeFutureSuffix(PrettyTime pt) {
        for (TimeUnit unit : pt.getUnits()) {
            ((ResourcesTimeFormat) pt.getFormat(unit)).setFutureSuffix("");
        }
        return pt;
    }
}
