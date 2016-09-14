package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.Menu;
import net.avicus.compendium.plugin.CompendiumTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;

/**
 * A type of menu that is a virtual chest inventory that stores items, associated
 * by an index.
 */
public class InventoryMenu implements Menu<InventoryMenuItem> {
    private final Plugin plugin;
    private final Player player;
    private final InventoryIndexer indexer;
    private final InventoryHandler handler;
    private final Inventory inventory;
    private final Map<Integer, InventoryMenuItem> items;
    private final Listener listener;

    public InventoryMenu(Plugin plugin,
                         Player player,
                         String title,
                         int rows) {
        this(plugin, player, title, rows, null, null);
    }

    public InventoryMenu(Plugin plugin,
                         Player player,
                         String title,
                         int rows,
                         Collection<InventoryMenuItem> items) {
        this(plugin, player, title, rows, null, null, items);
    }

    public InventoryMenu(Plugin plugin,
                         Player player,
                         String title,
                         int rows,
                         @Nullable InventoryHandler handler) {
        this(plugin, player, title, rows, null, handler);
    }

    public InventoryMenu(Plugin plugin,
                         Player player,
                         String title,
                         int rows,
                         @Nullable InventoryIndexer indexer) {
        this(plugin, player, title, rows, indexer, null);
    }

    public InventoryMenu(Plugin plugin,
                         Player player,
                         String title,
                         int rows,
                         @Nullable InventoryIndexer indexer,
                         @Nullable InventoryHandler handler) {
        this(plugin, player, title, rows, indexer, handler, null);
    }

    public InventoryMenu(Plugin plugin,
                  Player player,
                  String title,
                  int rows,
                  @Nullable InventoryIndexer indexer,
                  @Nullable InventoryHandler handler,
                  @Nullable Collection<InventoryMenuItem> items) {
        this.plugin = plugin;
        this.player = player;
        this.indexer = indexer == null ? new IndexedInventoryIndexer() : indexer;
        this.handler = handler == null ? new ClickableInventoryHandler() : handler;
        this.inventory = Bukkit.createInventory(this.player, rows * 9, title);
        this.items = this.indexer.getIndices(this, items == null ? new ArrayList<>() : items);
        this.listener = new InventoryMenuListener();
    }

    public void update(boolean force) {
        Map<Integer, InventoryMenuItem> oldIndices = this.items;
        Map<Integer, InventoryMenuItem> newIndices = this.indexer.getIndices(this, this.items.values());

        for (int i = 0; i < this.inventory.getSize(); i++) {
            InventoryMenuItem oldItem = oldIndices.get(i);
            InventoryMenuItem newItem = newIndices.get(i);

            boolean update = force || oldItem != newItem;
            boolean updateRequested = (oldItem != null && oldItem.shouldUpdate()) || (newItem != null && newItem.shouldUpdate());

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

        // Register listener
        HandlerList.unregisterAll(this.listener);
        this.player.getServer().getPluginManager().registerEvents(this.listener, this.plugin);

        // Open inventory if not open
        if (!Objects.equals(this.player.getOpenInventory(), this.inventory))
            this.player.openInventory(this.inventory);
    }

    @Override
    public void close() {
        // Unregister listener
        HandlerList.unregisterAll(this.listener);

        // Close inventory if open
        if (Objects.equals(this.player.getOpenInventory(), this.inventory))
            this.player.closeInventory();
    }

    /**
     * Called when a player exits the inventory menu.
     */
    public void onExit() {
        // Do nothing typically
    }

    /**
     * Add a new item to this menu.
     * @param item
     */
    public void add(InventoryMenuItem item) {
        // generate an index that is not used (can be negative, sorted later)
        int index = -1;
        while (this.items.containsKey(index))
            index--;
        this.items.put(index, item);
        update(true);
    }

    /**
     * Add multiple items to this menu.
     * @param items
     */
    public void add(Collection<InventoryMenuItem> items) {
        items.forEach(this::add);
    }

    /**
     * Remove an existing item from this menu.
     * @param item
     */
    public void remove(InventoryMenuItem item) {
        this.items.values().remove(item);
        update(true);
    }

    /**
     * Removes multiple items from this menu.
     * @param items
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
     * @param item The item.
     * @return The index.
     * @throws IllegalArgumentException If this menu does not contain the item.
     */
    public int getIndex(InventoryMenuItem item) throws IllegalArgumentException {
        for (int index : this.items.keySet())
            if (this.items.get(index).equals(item))
                return index;
        throw new IllegalArgumentException("Menu does not contain provided item.");
    }

    @Override
    public Collection<InventoryMenuItem> getItems() {
        return this.items.values();
    }

    /**
     * Handles clicks and closing the inventory.
     */
    private class InventoryMenuListener implements Listener {
        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {
            // Close menu if the inventory being opened is not this one
            if (event.getPlayer().equals(player) && !event.getInventory().equals(inventory))
                close();
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            // Close the menu if the player closes this inventory
            if (event.getPlayer().equals(player) && event.getInventory().equals(inventory)) {
                close();
                new CompendiumTask() {

                    @Override
                    public void run() throws Exception {
                        onExit();
                    }
                }.now();
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            // Close menu if player quits and had inventory open
            if (event.getPlayer().equals(player) && Objects.equals(event.getPlayer().getInventory(), inventory)) {
                close();
                onExit();
            }
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!event.getWhoClicked().equals(player))
                return;

            InventoryMenu menu = InventoryMenu.this;

            int index = event.getSlot();
            ItemStack clicked = event.getCurrentItem();

            if (event.getClickedInventory() == player.getInventory())
                return;

            Optional<InventoryMenuItem> item = Optional.empty();

            if (clicked != null && clicked.getType() != Material.AIR) {
                item = Optional.ofNullable(menu.items.get(index));

                if (!item.isPresent()) {
                    System.err.println("Clicked an ItemStack that isn't an InventoryMenuItem.");
                    return;
                }
            }

            menu.handler.onClick(menu, index, item, event.getClick());
            event.setCancelled(true);
        }
    }


}
