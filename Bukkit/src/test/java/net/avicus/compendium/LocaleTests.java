package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.LocalizedFormat;
import net.avicus.compendium.locale.UnlocalizedString;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Locale;

public class LocaleTests {
    private final Locale en = new Locale("en");

    @Test
    public void text() {
        System.out.println("\n=== TEXT ===");

        UnlocalizedText text = new UnlocalizedText("Hello, Mr. {0}!", new UnlocalizedString("Bill"));
        System.out.println(text.toComponent(null).toLegacyText());
    }

    @Test
    public void locale() {
        System.out.println("\n=== LOCALE ===");

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

    @Test
    public void constants() {
        System.out.println("\n=== CONSTANTS ===");

        System.out.println(ChatConstant.HELLO.toComponent(this.en).toLegacyText());
        System.out.println(ChatConstant.HELLO_SOMEONE.text().toComponent(this.en).toLegacyText());

        UnlocalizedText arg = new UnlocalizedText("Keenan");
        arg.bold(true);

        LocalizedFormat format = ChatConstant.HELLO_SOMEONE;

        LocalizedText text = format.text(arg);
        text.color(ChatColor.RED);

        System.out.println(text.toComponent(this.en).toLegacyText());

    }
}
