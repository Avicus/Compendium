package net.avicus.compendium;

public class MathUtil {
    public static boolean isInRange(Number toCheck, Number min, Number max, boolean useEquals) {
        long minLong = min.longValue();
        long maxLong = max.longValue();
        long checkLong = toCheck.longValue();

        if (useEquals)
            return (minLong <= checkLong && maxLong >= checkLong);
        else
            return (minLong < checkLong && maxLong > checkLong);
    }
}
