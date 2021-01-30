package net.avicus.compendium.commands.exception;

import com.sk89q.minecraft.util.commands.CommandException;
import lombok.Getter;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizableFormat;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import org.bukkit.ChatColor;

/**
 * A command exception that can be translated later on to be sent to players.
 */
public abstract class AbstractTranslatableCommandException extends CommandException {

  @Getter
  private final LocalizableFormat format;
  @Getter
  private final Localizable[] args;

  /**
   * Constructor
   *
   * @param format of the exception
   */
  public AbstractTranslatableCommandException(LocalizableFormat format) {
    this(format, Localizable.EMPTY);
  }

  /**
   * Constructor
   *
   * @param format of the exception
   * @param args for the format
   */
  public AbstractTranslatableCommandException(LocalizableFormat format, Localizable... args) {
    this.format = format;
    this.args = args;
  }

  /**
   * Format an exception with coloring and arguments to be sent to players.
   *
   * @param exception to format
   * @return a formatted {@link LocalizedText} from the exception
   */
  public static Localizable format(AbstractTranslatableCommandException exception) {
    final Localizable text;

    if (exception.args.length == 0) {
      text = exception.format.with();
    } else {
      text = exception.format.with(exception.args);
    }

    text.style().color(exception.getColor());
    return text;
  }

  /**
   * Format an error text from a format.
   *
   * @param format to create an error text for
   * @return a formatted {@link LocalizedText} from the format
   */
  public static LocalizedText error(LocalizedFormat format) {
    return error(format, Localizable.EMPTY);
  }

  /**
   * Format an error text from a format and arguments.
   *
   * @param format to create an error text for
   * @param args to supply to the format
   * @return a formatted {@link LocalizedText} from the format
   */
  public static LocalizedText error(LocalizedFormat format, Localizable... args) {
    LocalizedText text = format.with(args);
    text.style().color(ChatColor.RED);
    return text;
  }

  /**
   * @return the color of the exception
   */
  public abstract ChatColor getColor();
}
