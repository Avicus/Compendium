package net.avicus.compendium.locale.text;

import app.ashcon.sportpaper.api.text.PersonalizedComponent;
import net.avicus.compendium.TextStyle;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * Represents anything that can be translated and sent to players.
 */
public interface Localizable extends PersonalizedComponent {

  Localizable[] EMPTY = new Localizable[0];

  /**
   * Get the style of this.
   */
  TextStyle style();

  /**
   * Copy this and its styles.
   */
  Localizable duplicate();
}
