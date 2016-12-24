package net.avicus.compendium.settings.types;

import net.avicus.compendium.settings.SettingValue;
import net.avicus.compendium.settings.types.NumberSettingType.NumberSettingValue;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.concurrent.Immutable;

/**
 * Any type of number setting.
 * @param <N>
 */
@Immutable
public final class NumberSettingType<N extends Number> implements SettingType<NumberSettingValue<N>, N> {

    private final Function<String, N> function;

    protected NumberSettingType(Function<String, N> function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<NumberSettingValue<N>> parse(String raw) {
        try {
            N number = this.function.apply(raw);
            return Optional.of(value(number));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public NumberSettingValue<N> value(N raw) {
        return new NumberSettingValue<>(raw);
    }

    @Immutable
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
