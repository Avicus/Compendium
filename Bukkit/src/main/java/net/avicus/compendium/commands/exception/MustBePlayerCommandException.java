package net.avicus.compendium.commands.exception;

import net.avicus.compendium.plugin.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MustBePlayerCommandException extends TranslatableCommandErrorException {

    private MustBePlayerCommandException() {
        super(Messages.ERRORS_COMMAND_MUST_BE_PLAYER);
    }

    /**
     * Test if the specified command source is a {@link Player}.
     *
     * @param source the command source
     * @return the player
     * @throws MustBePlayerCommandException if the command source is not a {@link Player}
     */
    public static Player ensurePlayer(CommandSender source) throws MustBePlayerCommandException {
        if (!(source instanceof Player)) {
            throw new MustBePlayerCommandException();
        }
        return (Player) source;
    }
}
