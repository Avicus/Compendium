package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.ClickableMenuItem;

import java.util.Optional;

/**
 * A standard inventory handler that resolves clicks to ClickableMenuItem's onClick call.
 */
public class ClickableInventoryHandler implements InventoryHandler {
    @Override
    public void onClick(InventoryMenu menu, int index, Optional<InventoryMenuItem> item) {
        if (item.isPresent()) {
            InventoryMenuItem menuItem = item.get();
            if (menuItem instanceof ClickableMenuItem)
                ((ClickableMenuItem) menuItem).onClick();
        }
    }
}
