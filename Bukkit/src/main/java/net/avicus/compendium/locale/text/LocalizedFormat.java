package net.avicus.compendium.locale.text;

import com.google.common.base.Preconditions;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.LocaleBundle;

/**
 * Represents a format that takes arguments for localization with {@link LocalizedText}s.
 */
public class LocalizedFormat implements LocalizableFormat<LocalizedText> {
    private final LocaleBundle bundle;
    private final String key;

    public LocalizedFormat(LocaleBundle bundle, String key) {
        Preconditions.checkNotNull(bundle);
        Preconditions.checkNotNull(key);
        this.bundle = bundle;
        this.key = key;
    }

    @Override
    public LocalizedText with(TextStyle style, Localizable... arguments) {
        return new LocalizedText(this.bundle, this.key, style, arguments);
    }

    @Override
    public LocalizedText with(Localizable... arguments) {
        return new LocalizedText(this.bundle, this.key, arguments);
    }
}
