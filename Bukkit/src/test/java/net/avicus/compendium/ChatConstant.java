package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;

import java.util.Locale;

public class ChatConstant {
    private static LocaleBundle bundle;

    static {
        bundle = new LocaleBundle();

        Locale en = new Locale("en");
        Locale es = new Locale("es");

        LocaleStrings enStrings = new LocaleStrings();
        enStrings.add("hello", "Hello, {0}.");
        enStrings.add("name", "Alexander");

        LocaleStrings esStrings = new LocaleStrings();
        esStrings.add("hello", "Hola, {0}.");
        esStrings.add("name", "Alejandro");

        bundle.add(en, enStrings);
        bundle.add(es, esStrings);
    }

    public static LocalizedFormat HELLO = new LocalizedFormat(bundle, "hello");
    public static LocalizedFormat NAME = new LocalizedFormat(bundle, "name");
}
