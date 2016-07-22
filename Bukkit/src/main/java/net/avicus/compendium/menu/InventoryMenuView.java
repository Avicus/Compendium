package net.avicus.compendium.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class InventoryMenuView implements MenuView<InventoryMenuClick> {
    private final InventoryMenu menu;
    private final Player player;
    private Inventory inventory;

    public InventoryMenuView(InventoryMenu menu, Player player) {
        this.menu = menu;
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(this.player,  this.inventory.getSize());
        this.inventory.
    }

    @Override
    public void close() {
        this.player.closeInventory();
    }

    @Override
    public void onClick(InventoryMenuClick click) {

    }
}
