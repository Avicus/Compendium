package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.UnlocalizedString;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Locale;

public class LocaleTests {
    @Test
    public void text() {
        UnlocalizedText text = new UnlocalizedText("Hello, Mr. {0}!", new UnlocalizedString("Bill"));
        System.out.println(text.toComponent((Locale) null).toLegacyText());
    }

    @Test
    public void locale() {
        Locale en = new Locale("en");

        LocaleStrings strings = new LocaleStrings();
        strings.add("test-msg", "This is a {0} test.");
        strings.add("done-msg", "This test has {0}!");

        LocaleBundle bundle = new LocaleBundle();
        bundle.add(en, strings);

        // Strings
        {
            System.out.println(bundle.getString("test-msg", new UnlocalizedString("locale")).translate(en));
        }

        // Text
        {
            UnlocalizedText arg = new UnlocalizedText("completed").bold(true).color(ChatColor.RED);

            LocalizedText text = bundle.getText("done-msg", arg).color(ChatColor.DARK_RED).italic(true);

            System.out.println(text.toComponent(en).toLegacyText());
        }
    }
}
