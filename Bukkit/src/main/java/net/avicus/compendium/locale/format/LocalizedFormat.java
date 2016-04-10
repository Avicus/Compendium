package net.avicus.compendium.locale.format;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.text.LocalizableText;
import net.avicus.compendium.locale.text.LocalizedText;

public class LocalizedFormat implements LocalizableFormat {
    private final LocaleBundle bundle;
    private final String key;

    public LocalizedFormat(LocaleBundle bundle, String key) {
        this.bundle = bundle;
        this.key = key;
    }

    @Override
    public LocalizedText text(LocalizableText... arguments) {
        return new LocalizedText(this.bundle, this.key, arguments);
    }
}
