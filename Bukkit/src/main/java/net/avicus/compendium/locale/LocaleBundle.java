package net.avicus.compendium.locale;

import net.avicus.compendium.locale.text.LocalizedText;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class LocaleBundle {
    private Map<Locale, LocaleStrings> locales;
    private boolean defaultFallback;

    public LocaleBundle() {
        this.locales = new LinkedHashMap<>();
        this.defaultFallback = true;
    }

    public Optional<Locale> getDefaultLocale() {
        if (this.defaultFallback && this.locales.size() > 0)
            return Optional.of(this.locales.keySet().iterator().next());
        return Optional.empty();
    }

    public Optional<LocaleStrings> getDefaultStrings() {
        Optional<Locale> def = getDefaultLocale();
        if (def.isPresent())
            return Optional.of(this.locales.get(def.get()));
        return Optional.empty();
    }

    public Optional<LocaleStrings> getStringsRoughly(Locale locale) {
        if (this.locales.keySet().contains(locale))
            return Optional.of(this.locales.get(locale));

        for (Locale test : this.locales.keySet()) {
            if (test.getLanguage().equals(locale.getLanguage()))
                return Optional.of(this.locales.get(test));
        }

        return getDefaultStrings();
    }

    public void setDefaultFallback(boolean defaultFallback) {
        this.defaultFallback = defaultFallback;
    }

    public void add(Locale locale, LocaleStrings strings) {
        this.locales.put(locale, strings);
    }

    public Optional<String> get(Locale locale, String key) {
        Optional<LocaleStrings> strings = getStringsRoughly(locale);

        if (!strings.isPresent())
            return Optional.empty();

        Optional<String> result = strings.get().getString(key);

        if (result.isPresent())
            return Optional.of(result.get());

        Optional<LocaleStrings> defStrings = getDefaultStrings();

        if (!defStrings.isPresent() || strings.equals(getDefaultStrings()))
            return Optional.empty();

        return get(getDefaultLocale().get(), key);
    }

    public boolean has(Locale locale, String key) {
        return get(locale, key).isPresent();
    }

    public LocalizedString getString(String key, Localizable... arguments) {
        return new LocalizedString(this, key, arguments);
    }

    public LocalizedText getText(String key, LocalizableText... arguments) {
        LocalizedString string = getString(key);
        return new LocalizedText(string, arguments);
    }

    public LocalizedFormat getFormat(String key) {
        return new LocalizedFormat(this, key);
    }
}
