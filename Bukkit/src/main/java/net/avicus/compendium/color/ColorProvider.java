package net.avicus.compendium.color;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface ColorProvider {
    Color getColor(Optional<Player> player);
}
