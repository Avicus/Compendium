package net.avicus.compendium.menu.inventory;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class InventoryMenuAdapter implements InventoryHolder {

  @Getter(AccessLevel.PACKAGE)
  private final InventoryMenu menu;

  InventoryMenuAdapter(InventoryMenu menu) {
    this.menu = menu;
  }

  @Override
  public Inventory getInventory() {
    return this.menu.getInventory();
  }
}
