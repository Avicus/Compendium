package net.avicus.compendium.locale;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class LocaleBundle {
    private List<LocaleStrings> locales;

    public LocaleBundle() {
        this(new ArrayList<>());
    }

    public LocaleBundle(List<LocaleStrings> locales) {
        this.locales = locales;
    }

    public LocaleBundle(List<LocaleStrings> locales, LocaleStrings defaultStrings) {
        this.locales = locales;
        this.locales.remove(defaultStrings);
        this.locales.add(0, defaultStrings);
    }

    public Optional<Locale> getDefaultLocale() {
        Optional<LocaleStrings> strings = getDefaultStrings();
        if (strings.isPresent())
            return Optional.of(strings.get().getLocale());
        return Optional.empty();
    }

    public Optional<LocaleStrings> getDefaultStrings() {
        if (this.locales.size() > 0)
            return Optional.of(this.locales.get(0));
        return Optional.empty();
    }

    public Optional<LocaleStrings> getStringsRoughly(Locale locale) {
        LocaleStrings match = null;
        for (LocaleStrings test : this.locales) {
            if (test.getLocale().equals(locale))
                return Optional.of(test);
            else if (test.getLocale().getLanguage().equals(locale.getLanguage())) {
                match = test;
                break; // have a kitkat!
            }
        }

        if (match != null)
            return Optional.of(match);

        return getDefaultStrings();
    }

    public void add(LocaleStrings strings) {
        this.locales.add(strings);
    }

    public Optional<String> get(Locale locale, String key) {
        Optional<LocaleStrings> strings = getStringsRoughly(locale);

        if (!strings.isPresent())
            return Optional.empty();

        Optional<String> result = strings.get().get(key);

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

    public LocalizedFormat getFormat(String key) {
        return new LocalizedFormat(this, key);
    }

    public LocalizedText getText(String key, Localizable... arguments) {
        return new LocalizedText(this, key, arguments);
    }
}
