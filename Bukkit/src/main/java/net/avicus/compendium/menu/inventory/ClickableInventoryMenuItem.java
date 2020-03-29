package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.MenuItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A menu item that can be clicked, and handles clicks.
 */
public interface ClickableInventoryMenuItem extends MenuItem {

  void onClick(InventoryClickEvent event);
}
