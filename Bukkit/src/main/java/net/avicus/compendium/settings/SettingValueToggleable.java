package net.avicus.compendium.settings;

public interface SettingValueToggleable<R> extends SettingValue<R> {
    R next();
}
