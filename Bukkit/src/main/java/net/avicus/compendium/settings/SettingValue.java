package net.avicus.compendium.settings;

/**
 * Represents a type of which a setting can be stored.
 *
 * @param <R>
 */
public interface SettingValue<R> {
    /**
     * Gets the actual value.
     *
     * @return
     */
    R raw();

    /**
     * Serializes the value.
     *
     * @return
     */
    String serialize();
}
