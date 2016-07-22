package net.avicus.compendium.menu;

import org.bukkit.entity.Player;

public interface MenuView<T extends MenuClick> {
    Player getPlayer();

    void open();

    void close();

    void onClick(T click);
}
