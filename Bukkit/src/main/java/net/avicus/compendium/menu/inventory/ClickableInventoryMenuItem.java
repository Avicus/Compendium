package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.MenuItem;
import org.bukkit.event.inventory.ClickType;

/**
 * A menu item that can be clicked, and handles clicks.
 */
public interface ClickableInventoryMenuItem extends MenuItem {
    void onClick(ClickType type);
}
