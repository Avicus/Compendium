package net.avicus.compendium;

import java.util.Locale;
import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;

public class ChatConstant {

  private static LocaleBundle bundle;
  public static LocalizedFormat HELLO = new LocalizedFormat(bundle, "hello");
  public static LocalizedFormat NAME = new LocalizedFormat(bundle, "name");

  static {
    bundle = new LocaleBundle();

    LocaleStrings en = new LocaleStrings(new Locale("en"));
    en.add("hello", "Hello, {0}.");
    en.add("name", "Alexander");

    LocaleStrings es = new LocaleStrings(new Locale("es"));
    es.add("hello", "Hola, {0}.");
    es.add("name", "Alejandro");

    bundle.add(en);
    bundle.add(es);
  }
}
