package net.avicus.compendium.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class InventoryMenuClick implements MenuClick {
    private final Player player;
    private final ItemStack itemStack;
    private final int index;

    public InventoryMenuClick(Player player, ItemStack itemStack, int index) {
        this.player = player;
        this.itemStack = itemStack;
        this.index = index;
    }

    public Optional<ItemStack> getItemStack() {
        return Optional.of(this.itemStack);
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public Player getWhoClicked() {
        return this.player;
    }
}
