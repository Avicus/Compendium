package net.avicus.compendium.commands.exception;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizableFormat;
import org.bukkit.ChatColor;

/**
 * A command exception that can be translated later on to be sent to players, and is already
 * formatted as an error.
 */
public class TranslatableCommandErrorException extends AbstractTranslatableCommandException {

  public TranslatableCommandErrorException(LocalizableFormat format) {
    super(format);
  }

  public TranslatableCommandErrorException(LocalizableFormat format, Localizable... args) {
    super(format, args);
  }

  @Override
  public ChatColor getColor() {
    return ChatColor.RED;
  }
}
