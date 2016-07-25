package net.avicus.compendium.menu.inventory;

import java.util.Collection;
import java.util.Map;

/**
 * Assigns a list of menu items to slots.
 */
public interface InventoryIndexer {
    /**
     * Assign a collection of menu items to slots.
     * @param menu The menu.
     * @param items The items.
     * @return The map of index to inventory menu item.
     */
    Map<Integer, InventoryMenuItem> getIndices(InventoryMenu menu, Collection<InventoryMenuItem> items);
}
