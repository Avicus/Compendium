package net.avicus.compendium.menu.inventory;

import java.util.Optional;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Handles when a player clicks inside an opened inventory menu.
 */
public interface InventoryHandler {

  /**
   * Called when a player clicks inside an opened inventory menu.
   *
   * @param menu The inventory menu.
   * @param index The inventory index of the click.
   * @param item The menu item clicked.
   * @param event which the click created.
   */
  void onClick(InventoryMenu menu, int index, Optional<InventoryMenuItem> item,
      InventoryClickEvent event);
}
