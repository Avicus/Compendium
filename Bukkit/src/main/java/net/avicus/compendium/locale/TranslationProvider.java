package net.avicus.compendium.locale;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;
import org.bukkit.plugin.Plugin;
import org.jdom2.JDOMException;

public class TranslationProvider {

  public static final LocalizedFormat $NULL$ = new LocalizedFormat(null, null);
  private static final Field MODIFIERS_FIELD;
  private static final Joiner JOINER = Joiner.on(".");

  static {
    try {
      MODIFIERS_FIELD = Field.class.getDeclaredField("modifiers");
      MODIFIERS_FIELD.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * Map varaibles to the translations which they represent.
   *
   * @param clazz containing the variables to map
   * @param bundle containing the translations to get mappings from
   */
  public static void map(Class<?> clazz, LocaleBundle bundle) {
    try {
      for (Field field : clazz.getFields()) {
        field.setAccessible(true);
        MODIFIERS_FIELD.set(field, field.getModifiers() & ~Modifier.FINAL);
        if (field.get(null) == $NULL$) {
          field.set(null, bundle.getFormat(JOINER.join(field.getName().toLowerCase().split("_"))));
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

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
