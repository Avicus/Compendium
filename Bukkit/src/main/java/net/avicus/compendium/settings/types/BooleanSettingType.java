package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingValueToggleable;
import net.avicus.compendium.settings.types.BooleanSettingType.BooleanSettingValue;

import java.util.Optional;

import javax.annotation.concurrent.Immutable;

/**
 * True or false setting.
 */
@Immutable
public final class BooleanSettingType implements SettingType<BooleanSettingValue, Boolean> {

    private static final BooleanSettingValue TRUE = new BooleanSettingValue(true);
    private static final BooleanSettingValue FALSE = new BooleanSettingValue(false);

    protected BooleanSettingType() {
    }

    @Override
    public Optional<BooleanSettingValue> parse(String raw) {
        switch (raw.toLowerCase()) {
            case "true":
            case "on":
            case "yes":
                return Optional.of(TRUE);
            case "false":
            case "off":
            case "no":
                return Optional.of(FALSE);
            default:
                return Optional.empty();
        }
    }

    @Override
    public BooleanSettingValue value(Boolean raw) {
        return new BooleanSettingValue(raw);
    }

    @Immutable
    public static final class BooleanSettingValue implements SettingValueToggleable<Boolean> {
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
