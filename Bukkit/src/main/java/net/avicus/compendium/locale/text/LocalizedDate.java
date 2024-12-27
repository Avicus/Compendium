package net.avicus.compendium.locale.text;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

/**
 * A date object that can be translated to any locale with a specific style.
 */
public class LocalizedDate implements Localizable {

  private final Date date;
  private final TextStyle style;

  public LocalizedDate(Date date, TextStyle style) {
    this.date = date;
    this.style = style;
  }

  public LocalizedDate(Date date) {
    this(date, TextStyle.create());
  }


  @Override
  public BaseComponent render(CommandSender commandSender) {
    Locale locale = commandSender == null ? Locale.getDefault() : commandSender.getLocale();
    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
    return new UnlocalizedText(format.format(this.date), this.style).render(commandSender);
  }

  @Override
  public TextStyle style() {
    return this.style;
  }

  @Override
  public LocalizedDate duplicate() {
    return new LocalizedDate(this.date, this.style.duplicate());
  }
}
