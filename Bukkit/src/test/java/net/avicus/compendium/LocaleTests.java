package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Locale;

public class LocaleTests {
    @Test
    public void bundles() {
        System.out.println("=== BUNDLES ===");
        LocaleStrings en = new LocaleStrings();
        en.add("hello", "Hello {0}!");

        LocaleBundle bundle = new LocaleBundle();
        bundle.add(Locale.ENGLISH, en);

        System.out.println(bundle.get(Locale.ENGLISH, "hello"));

        UnlocalizedText keenan = new UnlocalizedText("Keenan");
        keenan.style().color(ChatColor.RED);

        LocalizedText text = new LocalizedText(bundle, "hello", keenan, keenan);
        text.style().color(ChatColor.DARK_RED).bold(true);

        System.out.println(text.translate(Locale.TRADITIONAL_CHINESE).toLegacyText());
    }

    @Test
    public void constants() {
        System.out.println("=== CONSTANTS ===");
        LocalizedFormat format = ChatConstant.HELLO;

        LocalizedText name = ChatConstant.NAME.with(TextStyle.ofColor(ChatColor.RED).bold(false));

        LocalizedText hello = format.with(TextStyle.ofBold(), name);

        System.out.println(hello.translate(new Locale("es")).toLegacyText());
    }
}
