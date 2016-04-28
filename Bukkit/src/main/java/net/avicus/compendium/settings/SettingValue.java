package net.avicus.compendium.settings;

public interface SettingValue<R> {
    R raw();

    String serialize();
}
