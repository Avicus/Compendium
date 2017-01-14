package net.avicus.compendium.utils;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.Normalizer;
import java.util.List;
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

    /**
     * Wrap a long {@link ItemMeta} lore line and append it to a list of lore with
     * last colors copied over.
     *
     * @param lore the result list
     * @param length the length to wrap on
     * @param string the string to wrap
     */
    public static void wrapLoreCopyColors(final List<String> lore, final int length, final String string) {
        String previous = "";
        for (String wrapped : WordUtils.wrap(string, length).split(SystemUtils.LINE_SEPARATOR)) {
            if (!previous.isEmpty()) {
                previous = ChatColor.getLastColors(previous);
            }

            lore.add(previous + wrapped);
            previous = wrapped;
        }
    }
}
