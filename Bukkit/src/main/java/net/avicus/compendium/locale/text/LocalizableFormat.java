package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;

public interface LocalizableFormat<T extends Localizable> {
    T with(TextStyle style, Localizable... arguments);

    T with(Localizable... arguments);

    default T with(TextStyle style, String... arguments) {
        T result = with(arguments);
        result.style().inherit(style);
        return result;
    }

    default T with(String... arguments) {
        Localizable[] localized = new Localizable[arguments.length];
        for (int i = 0; i < localized.length; i++)
            localized[i] = new UnlocalizedText(arguments[i]);
        return with(localized);
    }
}
