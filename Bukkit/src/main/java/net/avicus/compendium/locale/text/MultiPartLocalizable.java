package net.avicus.compendium.locale.text;

import java.util.Locale;
import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

/**
 * A {@link Localizable} that joins multiple {@link Localizable}s together.
 */
public class MultiPartLocalizable implements Localizable {

  private final Localizable[] args;

  public MultiPartLocalizable(Localizable... args) {
    this.args = args;
  }

  @Override
  public TextStyle style() {
    throw new UnsupportedOperationException("Multipart localizables do not have styling.");
  }

  @Override
  public Localizable duplicate() {
    return new MultiPartLocalizable(this.args);
  }


  @Override
  public BaseComponent render(CommandSender commandSender) {
    TextComponent message = new TextComponent(this.args[0].render(commandSender));

    for (int i = 1; i < this.args.length; i++) {
      message.addExtra(this.args[i].render(commandSender));
    }

    return message;
  }
}
