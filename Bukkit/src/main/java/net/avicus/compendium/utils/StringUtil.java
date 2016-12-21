package net.avicus.compendium.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class StringUtil {

    /** A pattern for non-latin characters. */
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    /** A pattern for whitespace characters. */
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    /** The string non-latin characters are replaced with. */
    private static final String REPLACEMENT = ""; // Empty space!

    private StringUtil() {
    }

    /**
     * Transform a string into a slugged string.
     *
     * @param string the original string
     * @return the slugged string
     */
    public static String slugify(String string) {
        return NON_LATIN.matcher(Normalizer.normalize(WHITESPACE.matcher(string).replaceAll("-"), Normalizer.Form.NFD))
            .replaceAll(REPLACEMENT)
            .toLowerCase(Locale.ENGLISH);
    }
}
