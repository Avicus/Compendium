package net.avicus.compendium.menu;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface MenuClick<T extends MenuItem> {
    Optional<T> getItem();

    Player getWhoClicked();
}
