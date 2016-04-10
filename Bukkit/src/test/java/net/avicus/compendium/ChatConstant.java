package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;

import java.util.Locale;

public class ChatConstant {
    private static final LocaleBundle bundle;

    static {
        bundle = new LocaleBundle();

        Locale en = new Locale("en");
        Locale es = new Locale("es");

        LocaleStrings strings = new LocaleStrings();
        strings.add("hello", "Hello.");
        strings.add("hello-someone", "Hello, {0}.");
        bundle.add(en, strings);

        strings = new LocaleStrings();
        strings.add("hello", "Hola.");
        strings.add("hello-someone", "Hola, {0}.");
        bundle.add(es, strings);
    }

    public static final LocalizedText HELLO = bundle.getFormat("hello").text();
    public static final LocalizedFormat HELLO_SOMEONE = bundle.getFormat("hello-someone");

}
