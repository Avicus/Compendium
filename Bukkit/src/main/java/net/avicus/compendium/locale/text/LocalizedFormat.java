package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.LocaleBundle;

public class LocalizedFormat implements LocalizableFormat<LocalizedText> {
    private final LocaleBundle bundle;
    private final String key;

    public LocalizedFormat(LocaleBundle bundle, String key) {
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
