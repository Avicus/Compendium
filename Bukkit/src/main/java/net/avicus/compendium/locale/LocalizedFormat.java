package net.avicus.compendium.locale;

import net.avicus.compendium.locale.text.LocalizedText;

public class LocalizedFormat {
    private final LocaleBundle bundle;
    private final String key;

    public LocalizedFormat(LocaleBundle bundle, String key) {
        this.bundle = bundle;
        this.key = key;
    }

    public LocalizedString string(Localizable... args) {
        return new LocalizedString(this.bundle, this.key, args);
    }

    public LocalizedText text(LocalizableText... args) {
        return new LocalizedText(new LocalizedString(this.bundle, this.key), args);
    }
}
