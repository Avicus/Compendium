package net.avicus.compendium.settings;

import lombok.Getter;

/**
 * Setting -> Value
 *
 * @param <S>
 * @param <R>
 */
public class SettingContext<S extends Setting<R>, R> {
    @Getter private final S setting;
    @Getter private final SettingValue<R> value;

    public SettingContext(S setting, SettingValue<R> value) {
        this.setting = setting;
        this.value = value;
    }
}
