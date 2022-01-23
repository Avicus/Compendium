package net.avicus.compendium.menu.inventory;

import java.util.Optional;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A standard inventory handler that resolves clicks to ClickableMenuItem's onClick call.
 */
public class ClickableInventoryHandler implements InventoryHandler {

  @Override
  public void onClick(InventoryMenu menu, int index, Optional<InventoryMenuItem> item,
      InventoryClickEvent event) {
    if (item.isPresent()) {
      InventoryMenuItem menuItem = item.get();
      if (menuItem instanceof ClickableInventoryMenuItem) {
        ((ClickableInventoryMenuItem) menuItem).onClick(event);
      }
    }
  }
}
