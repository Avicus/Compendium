package net.avicus.compendium.config.inject;

import net.avicus.compendium.config.ConfigException;

public class ConfigInjectionException extends ConfigException {

  public ConfigInjectionException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public ConfigInjectionException(String msg) {
    super(msg);
  }
}
