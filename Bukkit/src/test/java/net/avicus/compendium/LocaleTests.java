package net.avicus.compendium;

import net.avicus.compendium.locale.*;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

public class LocaleTests {
    private final Locale en = new Locale("en");
    private final Locale es = new Locale("es", "ES");

    @Test
    public void text() {
        System.out.println("\n=== TEXT ===");

        UnlocalizedText text = new UnlocalizedText("Hello, Mr. {0}!", new UnlocalizedText("Bill"));
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
            System.out.println(bundle.getText("test-msg", new UnlocalizedText("locale")).toLegacyText(en));
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

        // No args
        {
            System.out.println(ChatConstant.HELLO.text().toComponent(this.en).toLegacyText());
            System.out.println(ChatConstant.HELLO_SOMEONE.text().toComponent(this.en).toLegacyText());
        }

        // Right args, colors
        {
            LocalizedText alex = ChatConstant.ALEX.text().bold(true);
            LocalizedText text = ChatConstant.HELLO_SOMEONE.text(alex).color(ChatColor.RED);

            System.out.println(text.toComponent(this.en).toLegacyText());
            System.out.println(text.toComponent(this.es).toLegacyText());
        }

        // Date
        {
            LocalizedText text = ChatConstant.TIME_IS.text(new LocalizedDate(new Date(), LocalizedDate.Type.TIME));
            System.out.println(text.toPlainText(this.en));
            System.out.println(text.toPlainText(this.es));
        }

        // Money
        {
            LocalizedText text = ChatConstant.THAT_COSTS.text(new LocalizedNumber(5.49));
            System.out.println(text.toPlainText(this.en));
            System.out.println(text.toPlainText(this.es));
        }
    }
}
