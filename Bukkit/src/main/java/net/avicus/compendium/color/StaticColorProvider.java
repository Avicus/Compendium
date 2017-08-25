package net.avicus.compendium.color;

import java.util.Optional;
import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * Returns the same color regardless of the player supplied.
 */
public class StaticColorProvider implements ColorProvider {

  private final Color color;

  public StaticColorProvider(Color color) {
    this.color = color;
  }

  @Override
  public Color getColor(Optional<Player> player) {
    return this.color;
  }
}
