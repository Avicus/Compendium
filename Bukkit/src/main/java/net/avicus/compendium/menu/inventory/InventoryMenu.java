package net.avicus.compendium.menu.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import net.avicus.compendium.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A type of menu that is a virtual chest inventory that stores items, associated
 * by an index.
 */
public class InventoryMenu implements Menu<InventoryMenuItem> {

  private final Player player;
  private final InventoryIndexer indexer;
  @Getter(AccessLevel.PACKAGE)
  private final InventoryHandler handler;
  private final InventoryMenuAdapter adapter;
  @Getter(AccessLevel.PACKAGE)
  private final Inventory inventory;
  private final Map<Integer, InventoryMenuItem> items;

  public InventoryMenu(Player player,
      String title,
      int rows) {
    this(player, title, rows, null, null);
  }

  public InventoryMenu(Player player,
      String title,
      int rows,
      Collection<InventoryMenuItem> items) {
    this(player, title, rows, null, null, items);
  }

  public InventoryMenu(Player player,
      String title,
      int rows,
      @Nullable InventoryHandler handler) {
    this(player, title, rows, null, handler);
  }

  public InventoryMenu(Player player,
      String title,
      int rows,
      @Nullable InventoryIndexer indexer) {
    this(player, title, rows, indexer, null);
  }

  public InventoryMenu(Player player,
      String title,
      int rows,
      @Nullable InventoryIndexer indexer,
      @Nullable InventoryHandler handler) {
    this(player, title, rows, indexer, handler, null);
  }

  public InventoryMenu(Player player,
      String title,
      int rows,
      @Nullable InventoryIndexer indexer,
      @Nullable InventoryHandler handler,
      @Nullable Collection<InventoryMenuItem> items) {
    this.player = player;
    this.indexer = indexer == null ? new IndexedInventoryIndexer() : indexer;
    this.handler = handler == null ? new ClickableInventoryHandler() : handler;
    this.adapter = new InventoryMenuAdapter(this);
    this.inventory = Bukkit.createInventory(this.adapter, rows * 9, title);
    this.items = this.indexer.getIndices(this, items == null ? new ArrayList<>() : items);
  }

  public void update(boolean force) {
    Map<Integer, InventoryMenuItem> oldIndices = this.items;
    Map<Integer, InventoryMenuItem> newIndices = this.indexer.getIndices(this, this.items.values());

    for (int i = 0; i < this.inventory.getSize(); i++) {
      InventoryMenuItem oldItem = oldIndices.get(i);
      InventoryMenuItem newItem = newIndices.get(i);

      boolean update = force || oldItem != newItem;
      boolean updateRequested =
          (oldItem != null && oldItem.shouldUpdate()) || (newItem != null && newItem
              .shouldUpdate());

      if (update || updateRequested) {
        ItemStack stack = null;
        if (newItem != null) {
          stack = newItem.getItemStack();
          newItem.onUpdate();
        }
        this.inventory.setItem(i, null);
        this.inventory.setItem(i, stack);
      }
    }

    this.player.updateInventory();
    this.items.clear();
    this.items.putAll(newIndices);
  }

  @Override
  public void open() {
    // Update items
    update(true);

    // Open inventory if not open
    if (!Objects.equals(this.player.getOpenInventory(), this.inventory)) {
      this.player.openInventory(this.inventory);
    }
  }

  @Override
  public void close() {
    // Close inventory if open
    if (Objects.equals(this.player.getOpenInventory(), this.inventory)) {
      this.player.closeInventory();
    }
  }

  /**
   * Called when a player exits the inventory menu.
   */
  public void onExit() {
    // Do nothing typically
  }

  /**
   * Add a new item to this menu.
   */
  public void add(InventoryMenuItem item) {
    // generate an index that is not used (can be negative, sorted later)
    int index = -1;
    while (this.items.containsKey(index)) {
      index--;
    }
    this.items.put(index, item);
    update(true);
  }

  /**
   * Add multiple items to this menu.
   */
  public void add(Collection<InventoryMenuItem> items) {
    items.forEach(this::add);
  }

  /**
   * Remove an existing item from this menu.
   */
  public void remove(InventoryMenuItem item) {
    this.items.values().remove(item);
    update(true);
  }

  /**
   * Removes multiple items from this menu.
   */
  public void removeAll(Collection<InventoryMenuItem> items) {
    this.items.values().removeAll(items);
    update(true);
  }

  /**
   * Removes all items from the menu.
   */
  public void clear() {
    this.items.clear();
    update(true);
  }

  /**
   * Get the current index of a specific menu item.
   *
   * @param item The item.
   * @return The index.
   * @throws IllegalArgumentException If this menu does not contain the item.
   */
  public int getIndex(InventoryMenuItem item) throws IllegalArgumentException {
    for (int index : this.items.keySet()) {
      if (this.items.get(index).equals(item)) {
        return index;
      }
    }
    throw new IllegalArgumentException("Menu does not contain provided item.");
  }

  @Override
  public Collection<InventoryMenuItem> getItems() {
    return this.items.values();
  }

  Map<Integer, InventoryMenuItem> getItemMap() {
    return this.items;
  }
}
