package net.avicus.compendium.commands.exception;

import com.sk89q.minecraft.util.commands.CommandException;
import lombok.Getter;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import org.bukkit.ChatColor;

public abstract class AbstractTranslatableCommandException extends CommandException {

    @Getter private final LocalizedFormat format;
    @Getter private final Localizable[] args;

    public AbstractTranslatableCommandException(LocalizedFormat format) {
        this(format, Localizable.EMPTY);
    }

    public AbstractTranslatableCommandException(LocalizedFormat format, Localizable... args) {
        this.format = format;
        this.args = args;
    }

    public abstract ChatColor getColor();

    public static LocalizedText format(AbstractTranslatableCommandException exception) {
        final LocalizedText text;

        if(exception.args.length == 0) {
            text = exception.format.with();
        }
        else {
            text = exception.format.with(exception.args);
        }

        text.style().color(exception.getColor());
        return text;
    }

    public static LocalizedText error(LocalizedFormat format) {
        return error(format, Localizable.EMPTY);
    }

    public static LocalizedText error(LocalizedFormat format, Localizable... args) {
        LocalizedText text = format.with(args);
        text.style().color(ChatColor.RED);
        return text;
    }
}
