package net.avicus.compendium.color;

import java.util.Optional;
import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * A generic interface to get a color based on the player who is viewing it.
 */
public interface ColorProvider {

  Color getColor(Optional<Player> player);
}
