package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.MenuItem;
import org.bukkit.inventory.ItemStack;

public interface InventoryMenuItem extends MenuItem {
    ItemStack getItemStack();

    boolean update();
}
