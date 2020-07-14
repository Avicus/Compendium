package net.avicus.compendium.locale;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import net.avicus.compendium.utils.FileBackedKVSet;
import net.md_5.bungee.api.ChatColor;

/**
 * A key->value represtantation of strings for a specific {@link Locale} that can be retrieved for
 * localization.
 */
@ToString
public class LocaleStrings extends FileBackedKVSet<String> {

  @Getter
  private final Locale locale;

  /**
   * Constructor.
   *
   * @param locale that this collection of strings is for
   */
  public LocaleStrings(Locale locale) {
    this(locale, new HashMap<>());
  }

  /**
   * Constructor.
   *
   * @param locale that this collection of strings is for
   * @param strings that make up this collection
   */
  public LocaleStrings(Locale locale, Map<String, String> strings) {
    super(strings);
    this.locale = locale;
  }

  @Override
  protected String parse(String raw) {
    return addColors(raw);
  }

  /**
   * Add colors to a message using {@link ChatColor#translateAlternateColorCodes(char, String)}.
   *
   * @param message to render colors for
   * @return a colored message
   */
  public static String addColors(String message) {
    if (message == null) {
      return null;
    }
    return ChatColor.translateAlternateColorCodes('^', message);
  }

  /**
   * Load in strings from a collection of .properties files and add them to the collection.
   *
   * @param root path of the translation files
   * @param locale that the strings are for
   * @return an object containing all of the loaded strings
   * @throws IOException if a file fails to load
   */
  public static LocaleStrings load(Path root, Locale locale) throws IOException {
    LocaleStrings strings = new LocaleStrings(locale);
    strings.load(root);
    return strings;
  }

  @Override
  public String toString() {
    return "LocaleStrings{" + "locale=" + locale + "} " + super.toString();
  }
}
