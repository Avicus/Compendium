package net.avicus.compendium.locale;

public class LocalizedFormat {
    private final LocaleBundle bundle;
    private final String key;

    public LocalizedFormat(LocaleBundle bundle, String key) {
        this.bundle = bundle;
        this.key = key;
    }

    public LocalizedText text(LocalizableText... arguments) {
        return new LocalizedText(this.bundle, this.key, arguments);
    }
}
