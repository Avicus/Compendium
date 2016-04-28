package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingType;
import net.avicus.compendium.settings.SettingValue;
import net.avicus.compendium.settings.types.NumberSettingType.NumberSettingValue;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Any type of number setting.
 * @param <N>
 */
public class NumberSettingType<N extends Number> implements SettingType<NumberSettingValue<N>, N> {
    private final Class<N> type;

    public NumberSettingType(Class<N> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<NumberSettingValue<N>> parse(String raw) {
        try {
            Method valueOf = this.type.getDeclaredMethod("valueOf", String.class);
            valueOf.setAccessible(true);

            N number = (N) valueOf.invoke(null, raw);
            return Optional.of(value(number));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public NumberSettingValue<N> value(N raw) {
        return new NumberSettingValue<N>(raw);
    }

    public static class NumberSettingValue<N extends Number> implements SettingValue<N> {
        private final N raw;

        public NumberSettingValue(N raw) {
            this.raw = raw;
        }

        @Override
        public N raw() {
            return this.raw;
        }

        @Override
        public String serialize() {
            return String.valueOf(this.raw);
        }
    }
}
