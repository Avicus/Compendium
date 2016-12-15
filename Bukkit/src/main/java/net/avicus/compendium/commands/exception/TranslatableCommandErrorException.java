package net.avicus.compendium.commands.exception;

import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedFormat;
import org.bukkit.ChatColor;

public class TranslatableCommandErrorException extends AbstractTranslatableCommandException {

    public TranslatableCommandErrorException(LocalizedFormat format) {
        super(format);
    }

    public TranslatableCommandErrorException(LocalizedFormat format, Localizable... args) {
        super(format, args);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }
}