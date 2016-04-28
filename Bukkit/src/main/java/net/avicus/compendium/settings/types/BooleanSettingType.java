package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingType;
import net.avicus.compendium.settings.SettingValueToggleable;
import net.avicus.compendium.settings.types.BooleanSettingType.BooleanSettingValue;

import java.util.Optional;

/**
 * True or false setting.
 */
public class BooleanSettingType implements SettingType<BooleanSettingValue, Boolean> {
    @Override
    public Optional<BooleanSettingValue> parse(String raw) {
        raw = raw.toLowerCase();
        switch (raw) {
            case "true":
            case "on":
            case "yes":
                return Optional.of(value(true));
            case "false":
            case "off":
            case "no":
                return Optional.of(value(false));
            default:
                return Optional.empty();
        }
    }

    @Override
    public BooleanSettingValue value(Boolean raw) {
        return new BooleanSettingValue(raw);
    }

    public static class BooleanSettingValue implements SettingValueToggleable<Boolean> {
        private final boolean value;

        public BooleanSettingValue(boolean value) {
            this.value = value;
        }

        @Override
        public Boolean raw() {
            return this.value;
        }

        @Override
        public String serialize() {
            return this.value ? "on" : "off";
        }

        @Override
        public Boolean next() {
            return !this.value;
        }
    }
}
