package net.avicus.compendium.locale;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocaleStrings {
    private final Map<String, String> strings;

    public LocaleStrings() {
        this.strings = new HashMap<>();
    }

    public void add(String key, String value) {
        this.strings.put(key, value);
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(this.strings.get(key));
    }
}
