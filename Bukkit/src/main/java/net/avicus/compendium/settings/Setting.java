package net.avicus.compendium.settings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.avicus.compendium.locale.text.Localizable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents a configurable option, a setting silly!
 * @param <R>
 */
@EqualsAndHashCode
public class Setting<R> {
    @Getter private final String id;
    @Getter private final SettingType<?, R> type;
    @Getter private final R defaultValue;
    @Getter private final Localizable name;
    @Getter private final List<Localizable> aliases;
    @Getter private final Localizable summary;
    @Getter private final Optional<Localizable> description;

    public <V extends SettingValue<R>> Setting(String id,
                                               SettingType<V, R> type,
                                               R defaultValue,
                                               Localizable name,
                                               List<Localizable> aliases,
                                               Localizable summary) {
        this(id, type, defaultValue, name, aliases, summary, Optional.empty());
    }

    public <V extends SettingValue<R>> Setting(String id,
                                               SettingType<V, R> type,
                                               R defaultValue,
                                               Localizable name,
                                               List<Localizable> aliases,
                                               Localizable summary,
                                               Optional<Localizable> description) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
        this.name = name;
        this.aliases = aliases;
        this.summary = summary;
        this.description = description;
    }

    public List<String> getAllAliases(Locale locale) {
        List<String> result = new ArrayList<>();
        result.add(this.name.translate(locale).toPlainText());
        for (Localizable alias : this.aliases)
            result.add(alias.translate(locale).toPlainText());
        return result;
    }

    public static Optional<Setting> search(Locale locale, String query, List<Setting> settings) {
        for (Setting<?> setting : settings) {
            List<String> names = setting.getAllAliases(locale);
            for (String name : names) {
                if (name.equalsIgnoreCase(query)) {
                    return Optional.of(setting);
                }
            }
        }
        return Optional.empty();
    }
}