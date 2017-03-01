package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import org.bukkit.ChatColor;

/**
 * Represents a format that takes arguments for localization.
 */
public interface LocalizableFormat<T extends Localizable> {

    T with(TextStyle style, Localizable... arguments);

    T with(Localizable... arguments);

    default T with() {
        return with(Localizable.EMPTY);
    }

    default T with(ChatColor color, Localizable... arguments) {
        return with(TextStyle.ofColor(color), arguments);
    }

    default T with(ChatColor color) {
        return with(color, new String[]{});
    }

    default T with(TextStyle style) {
        return with(style, Localizable.EMPTY);
    }

    default T with(TextStyle style, String... arguments) {
        T result = with(arguments);
        result.style().reset().inherit(style);
        return result;
    }

    default T with(ChatColor color, String... arguments) {
        return with(TextStyle.ofColor(color), arguments);
    }

    default T with(String... arguments) {
        Localizable[] localized = new Localizable[arguments.length];
        for (int i = 0; i < localized.length; i++)
            localized[i] = new UnlocalizedText(arguments[i]);
        return with(localized);
    }
}
