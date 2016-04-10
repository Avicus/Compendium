package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.format.LocalizedFormat;

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
        strings.add("alex", "Alexander");
        strings.add("time-is", "The time is {0}.");
        strings.add("that-costs", "That costs {0}.");
        bundle.add(en, strings);

        strings = new LocaleStrings();
        strings.add("hello", "Hola.");
        strings.add("hello-someone", "Hola, {0}.");
        strings.add("time-is", "La hora es {0}.");
        strings.add("that-costs", "Eso cuesta {0}.");
        bundle.add(es, strings);
    }

    public static final LocalizedFormat HELLO = bundle.getFormat("hello");
    public static final LocalizedFormat HELLO_SOMEONE = bundle.getFormat("hello-someone");
    public static final LocalizedFormat ALEX = bundle.getFormat("alex");
    public static final LocalizedFormat TIME_IS = bundle.getFormat("time-is");
    public static final LocalizedFormat THAT_COSTS = bundle.getFormat("that-costs");

}
