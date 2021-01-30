package net.avicus.compendium.commands.exception;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizableFormat;
import org.bukkit.ChatColor;

/**
 * A command exception that can be translated later on to be sent to players, and is already
 * formatted as a warning.
 */
public class TranslatableCommandWarningException extends AbstractTranslatableCommandException {

  public TranslatableCommandWarningException(LocalizableFormat format) {
    super(format);
  }

  public TranslatableCommandWarningException(LocalizableFormat format, Localizable... args) {
    super(format, args);
  }

  @Override
  public ChatColor getColor() {
    return ChatColor.YELLOW;
  }
}
