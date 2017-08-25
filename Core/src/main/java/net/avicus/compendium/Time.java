package net.avicus.compendium;

import java.util.Locale;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.TimeUnit;
import org.ocpsoft.prettytime.impl.ResourcesTimeFormat;
import org.ocpsoft.prettytime.units.JustNow;

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

  public static long[] secToMs(final double s1, final double s2, final double s3,
      final double s4, final double s5, final double s6) {
    return new long[]{(long) (s1 * 1000d), (long) (s2 * 1000d), (long) (s3 * 1000d),
        (long) (s4 * 1000d), (long) (s5 * 1000d), (long) (s6 * 1000d)};
  }

  public static long[] secToMs(final double s1) {
    final long v = (long) (s1 * 1000d);
    return new long[]{v, v, v, v, v, v};
  }
}
