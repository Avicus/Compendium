package net.avicus.compendium.menu;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface Menu<T extends MenuView> {
    Optional<T> getView(Player player);

    T open(Player player);

    Optional<T> close(Player player);
}
