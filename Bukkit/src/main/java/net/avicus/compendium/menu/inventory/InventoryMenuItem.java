package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.MenuItem;
import org.bukkit.inventory.ItemStack;

public interface InventoryMenuItem extends MenuItem {
    /**
     * The item stack display in the inventory.
     * @return
     */
    ItemStack getItemStack();

    /**
     * Check if the inventory should update the display of this item
     * (by subsequently calling {@link #getItemStack()}
     * @return
     */
    boolean shouldUpdate();

    /**
     * Called after updating the display of this item.
     */
    void onUpdate();
}
