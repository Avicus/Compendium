package net.avicus.compendium.commands.exception;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedFormat;
import org.bukkit.ChatColor;

public class TranslatableCommandWarningException extends AbstractTranslatableCommandException {

    public TranslatableCommandWarningException(LocalizedFormat format) {
        super(format);
    }

    public TranslatableCommandWarningException(LocalizedFormat format, Localizable... args) {
        super(format, args);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }
}
