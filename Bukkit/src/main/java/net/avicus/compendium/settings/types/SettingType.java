package net.avicus.compendium.settings.types;

import java.util.Optional;
import net.avicus.compendium.settings.SettingValue;

/**
 * Represents a type of setting.
 */
public interface SettingType<V extends SettingValue<R>, R> {

  /**
   * Parses a string.
   *
   * @return Empty if the input is invalid, otherwise the parsed value.
   */
  Optional<V> parse(String raw);

  /**
   * Gets the value instance based on raw data.
   */
  V value(R raw);
}
