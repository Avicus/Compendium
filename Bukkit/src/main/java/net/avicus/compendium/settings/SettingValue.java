package net.avicus.compendium.settings;

/**
 * Represents a type of which a setting can be stored.
 */
public interface SettingValue<R> {

  /**
   * Gets the actual value.
   */
  R raw();

  /**
   * Serializes the value.
   */
  String serialize();
}
