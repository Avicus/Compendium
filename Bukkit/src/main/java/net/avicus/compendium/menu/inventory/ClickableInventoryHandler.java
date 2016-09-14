package net.avicus.compendium.menu.inventory;

import org.bukkit.event.inventory.ClickType;

import java.util.Optional;

/**
 * A standard inventory handler that resolves clicks to ClickableMenuItem's onClick call.
 */
public class ClickableInventoryHandler implements InventoryHandler {
    @Override
    public void onClick(InventoryMenu menu, int index, Optional<InventoryMenuItem> item, ClickType clickType) {
        if (item.isPresent()) {
            InventoryMenuItem menuItem = item.get();
            if (menuItem instanceof ClickableInventoryMenuItem)
                ((ClickableInventoryMenuItem) menuItem).onClick(clickType);
        }
    }
}
