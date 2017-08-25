package net.avicus.compendium.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.settings.types.SettingType;

/**
 * Represents a configurable option, a setting silly!
 */
@EqualsAndHashCode
public class Setting<R> {

  @Getter
  private final String id;
  @Getter
  private final SettingType<?, R> type;
  @Getter
  private final R defaultValue;
  @Getter
  private final Localizable name;
  @Getter
  private final List<Localizable> aliases;
  @Getter
  private final Localizable summary;
  @Getter
  private final Optional<Localizable> description;

  public <V extends SettingValue<R>> Setting(String id,
      SettingType<V, R> type,
      R defaultValue,
      Localizable name,
      Localizable summary) {
    this(id, type, defaultValue, name, Collections.emptyList(), summary, Optional.empty());
  }

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

  public List<String> getAllAliases(Locale locale) {
    List<String> result = new ArrayList<>();
    result.add(this.name.translate(locale).toPlainText());
    result.addAll(this.aliases.stream()
        .map(alias -> alias.translate(locale)
            .toPlainText())
        .collect(Collectors.toList()));
    return result;
  }
}
