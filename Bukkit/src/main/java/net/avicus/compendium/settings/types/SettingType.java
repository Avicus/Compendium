package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingValue;

import java.util.Optional;

/**
 * Represents a type of setting.
 *
 * @param <V>
 * @param <R>
 */
public interface SettingType<V extends SettingValue<R>, R> {
    /**
     * Parses a string.
     *
     * @param raw
     * @return Empty if the input is invalid, otherwise the parsed value.
     */
    Optional<V> parse(String raw);

    /**
     * Gets the value instance based on raw data.
     *
     * @param raw
     * @return
     */
    V value(R raw);
}
