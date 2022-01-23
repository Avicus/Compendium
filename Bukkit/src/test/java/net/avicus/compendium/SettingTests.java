package net.avicus.compendium;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.SettingStore;
import net.avicus.compendium.settings.types.SettingTypes;
import org.junit.Test;

public class SettingTests {

  private static final Setting<Boolean> JOIN_MESSAGES = new Setting<>(
      "join-messages",
      SettingTypes.BOOLEAN,
      true,
      new UnlocalizedText("Join Messages"),
      Collections.emptyList(),
      new UnlocalizedText("Show or hide join messages."));

  private static final Setting<Often> SPAM = new Setting<>(
      "spam",
      SettingTypes.enumOf(Often.class),
      Often.NEVER,
      new UnlocalizedText("Spam Amount"),
      Collections.emptyList(),
      new UnlocalizedText("Configure how often we should spam you."));

  private static final Setting<Integer> VIEW_DISTANCE = new Setting<>(
      "view-distance",
      SettingTypes.INTEGER,
      5,
      new UnlocalizedText("View Distance"),
      Collections.emptyList(),
      new UnlocalizedText("Set your view distance."));

  @Test
  public void settings() {
    SettingStore store = new SettingStore();

    UUID jimmy = UUID.randomUUID();

    System.out.println("Default Setting");
    System.out.println(store.get(jimmy, JOIN_MESSAGES));

    System.out.println("Modified Setting");
    store.set(jimmy, JOIN_MESSAGES, false);
    System.out.println(store.get(jimmy, JOIN_MESSAGES));

    System.out.println("Default Enum");
    System.out.println(store.get(jimmy, SPAM));

    System.out.println("Modified Enum");
    store.set(jimmy, SPAM, Often.ALWAYS);
    System.out.println(store.get(jimmy, SPAM));

    System.out.println("Toggled Enum");
    System.out.println(store.toggle(jimmy, SPAM));
    System.out.println(store.get(jimmy, SPAM));

    System.out.println("Numbers");
    System.out.println(store.get(jimmy, VIEW_DISTANCE));
    System.out.println(store.set(jimmy, VIEW_DISTANCE, 2));

    System.out.println("=== Output ===");
    System.out.println(store);

    SettingStore clone = new SettingStore();
    clone.set(store.get(), Arrays.asList(JOIN_MESSAGES, SPAM, VIEW_DISTANCE));
    System.out.println("=== Clone ===");
    System.out.println(clone);

    assert store.equals(clone);
  }

  enum Often {
    ALWAYS,
    SOMETIMES,
    NEVER
  }
}
