package net.avicus.compendium.menu.inventory;

import java.util.Collection;
import java.util.Map;

public interface InventoryIndexer {
    Map<Integer, InventoryMenuItem> getIndices(InventoryMenu view, Collection<InventoryMenuItem> items);
}
