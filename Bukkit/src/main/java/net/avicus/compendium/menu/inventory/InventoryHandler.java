package net.avicus.compendium.menu.inventory;

import java.util.Optional;

public interface InventoryHandler {
    void onClick(InventoryMenu menu, int index, Optional<InventoryMenuItem> item);
}
