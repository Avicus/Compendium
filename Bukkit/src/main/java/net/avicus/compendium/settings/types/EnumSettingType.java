package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingType;
import net.avicus.compendium.settings.SettingValueToggleable;
import net.avicus.compendium.settings.types.EnumSettingType.EnumSettingValue;

import java.util.Optional;

public class EnumSettingType<E extends Enum> implements SettingType<EnumSettingValue<E>, E> {
    private final Class<E> type;

    public EnumSettingType(Class<E> type) {
        this.type = type;
    }

    @Override
    public Optional<EnumSettingValue<E>> parse(String raw) {
        raw = raw.toLowerCase();

        E[] options = this.type.getEnumConstants();
        E result = null;
        E close = null;

        for (E option : options) {
            if (option.name().equalsIgnoreCase(raw)) {
                result = option;
                break;
            }
            else if (option.name().toLowerCase().startsWith(raw)) {
                close = option;
            }
        }

        if (result == null)
            result = close;

        if (result == null)
            return Optional.empty();

        return Optional.of(parse(result));
    }

    @Override
    public EnumSettingValue<E> parse(E raw) {
        return new EnumSettingValue<>(this.type, raw);
    }

    public static class EnumSettingValue<E extends Enum> implements SettingValueToggleable<E> {
        private final Class<E> type;
        private final E raw;

        public EnumSettingValue(Class<E> type, E raw) {
            this.type = type;
            this.raw = raw;
        }

        @Override
        public E raw() {
            return this.raw;
        }

        @Override
        public String serialize() {
            return this.raw.name();
        }

        @Override
        public E next() {
            E[] options = this.type.getEnumConstants();
            int current = this.raw.ordinal();

            if (current + 1 >= options.length)
                return options[0];

            return options[current + 1];
        }
    }
}
