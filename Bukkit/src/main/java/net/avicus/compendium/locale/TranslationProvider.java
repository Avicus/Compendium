package net.avicus.compendium.locale;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.avicus.compendium.locale.text.LocalizedFormat;
import org.bukkit.Bukkit;

public class TranslationProvider {

  public static final LocalizedFormat $NULL$ = new LocalizedFormat(null, null);
  private static final Joiner JOINER = Joiner.on(".");

  /**
   * Load translations for a set of locales.
   *
   * @param basePath containing the files with translations
   * @param locales to load
   * @return a bundle containing all of the loaded data
   */
  public static LocaleBundle loadBundle(String basePath, String... locales) {
    try {
      final List<LocaleStrings> list = new ArrayList<>();
      final LocaleStrings english = getLocaleStrings(basePath, "en_US", Locale.ENGLISH);
      for (String locale : locales) {
        list.add(
            getLocaleStrings(basePath, String.format("%s", locale), Locale.forLanguageTag(locale)));
      }
      return new LocaleBundle(list, english);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load translations: " + e.getMessage(), e);
    }
  }

  private static LocaleStrings getLocaleStrings(String basePath, String resource, Locale locale)
      throws IOException {
    return LocaleStrings.load(Paths.get(basePath, resource), locale);
  }
}
