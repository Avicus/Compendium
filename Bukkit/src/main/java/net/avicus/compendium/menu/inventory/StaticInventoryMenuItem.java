package net.avicus.compendium.menu.inventory;

/**
 * An itemstack menu item that does not change.
 */
public abstract class StaticInventoryMenuItem implements InventoryMenuItem {
    @Override
    public boolean update() {
        return false;
    }
}
