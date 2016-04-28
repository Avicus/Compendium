package net.avicus.compendium.settings;

import lombok.Getter;

public class SettingContext<S extends Setting<R>, R> {
    @Getter private final S setting;
    @Getter private final SettingValue<R> value;

    public SettingContext(S setting, SettingValue<R> value) {
        this.setting = setting;
        this.value = value;
    }
}
