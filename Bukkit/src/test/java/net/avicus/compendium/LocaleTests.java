package net.avicus.compendium;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.ChatColor;
import org.jdom2.JDOMException;
import org.junit.Test;

public class LocaleTests {

  @Test
  public void load() throws JDOMException, IOException, URISyntaxException {
    URL resource = this.getClass().getClassLoader().getResource("./");
    LocaleStrings strings = new LocaleStrings(Locale.ENGLISH);
    strings.load(Paths.get(resource.toURI()));
    System.out.println(strings);
  }

  @Test
  public void bundles() {
    System.out.println("=== BUNDLES ===");
    LocaleStrings en = new LocaleStrings(new Locale("en"));
    en.add("hello", "Hello {0}!");

    LocaleBundle bundle = new LocaleBundle();
    bundle.add(en);

    System.out.println(bundle.get(Locale.ENGLISH, "hello"));

    UnlocalizedText keenan = new UnlocalizedText("Keenan");
    keenan.style().color(ChatColor.RED);

    LocalizedText text = new LocalizedText(bundle, "hello", keenan, keenan);
    text.style().color(ChatColor.DARK_RED).bold(true);

    System.out.println(text.render(null).toLegacyText());
  }

  @Test
  public void constants() {
    System.out.println("=== CONSTANTS ===");
    LocalizedFormat format = ChatConstant.HELLO;

    LocalizedText name = ChatConstant.NAME.with(TextStyle.ofColor(ChatColor.RED).bold(false));

    LocalizedText hello = format.with(TextStyle.ofBold(), name);

    System.out.println(hello.render(null).toLegacyText());
  }
}
