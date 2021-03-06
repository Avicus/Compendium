package net.avicus.compendium.settings;

/**
 * Represents a type of setting that can be toggled or set without a value.
 */
public interface SettingValueToggleable<R> extends SettingValue<R> {

  /**
   * Retrieve the next raw data after this value.
   */
  R next();
}
