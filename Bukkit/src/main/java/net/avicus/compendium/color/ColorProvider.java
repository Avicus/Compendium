package net.avicus.compendium.color;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * A generic interface to get a color based on the player who is viewing it.
 */
public interface ColorProvider {
    Color getColor(Optional<Player> player);
}
