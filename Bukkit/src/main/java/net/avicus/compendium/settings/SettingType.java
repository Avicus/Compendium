package net.avicus.compendium.settings;

import java.util.Optional;

public interface SettingType<V extends SettingValue<R>, R> {
    Optional<V> parse(String raw);

    V parse(R raw);
}
