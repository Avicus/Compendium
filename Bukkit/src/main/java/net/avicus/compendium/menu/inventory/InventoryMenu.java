package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.Menu;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A type of menu that is a virtual chest inventory that stores items, associated
 * by an index.
 */
public class InventoryMenu implements Menu {
    private final Plugin plugin;
    private final Player player;
    private final InventoryIndexer indexer;
    private final InventoryHandler handler;
    private final Map<Integer, InventoryMenuItem> items;
    private final int rows;
    private final Listener listener;
    private Inventory inventory;

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows) {
        this(plugin, player, items, rows, new IndexedInventoryIndexer(), new ClickableInventoryHandler());
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryHandler handler) {
        this(plugin, player, items, rows, new IndexedInventoryIndexer(), handler);
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryIndexer indexer) {
        this(plugin, player, items, rows, indexer, new ClickableInventoryHandler());
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryHandler handler, InventoryIndexer indexer) {
        this(plugin, player, items, rows, indexer, handler);
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryIndexer indexer, InventoryHandler handler) {
        this.plugin = plugin;
        this.player = player;
        this.rows = rows;
        this.indexer = indexer;
        this.handler = handler;
        this.items = this.indexer.getIndices(this, items);
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
                ItemStack stack = newItem == null ? null : newItem.getItemStack();
                this.inventory.setItem(i, stack);
            }
        }
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(this.player, this.rows * 9);
        this.player.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
        this.player.openInventory(this.inventory);
        update(true);
    }

    @Override
    public void close() {
        HandlerList.unregisterAll(this.listener);
    }

    @Override
    public Collection getItems() {
        return this.items.values();
    }

    /**
     * Handles clicks and closing the inventory.
     */
    private class InventoryMenuListener implements Listener {
        @EventHandler
        public void onInventoryClose(InventoryOpenEvent event) {
            if (!event.getInventory().equals(inventory))
                close();
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getPlayer().equals(player))
                close();
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            if (event.getPlayer().equals(player))
                close();
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!event.getWhoClicked().equals(player))
                return;

            InventoryMenu menu = InventoryMenu.this;

            int index = event.getSlot();
            ItemStack clicked = event.getCurrentItem();

            Optional<InventoryMenuItem> item = Optional.empty();

            if (clicked != null && clicked.getType() != Material.AIR) {
                item = Optional.ofNullable(menu.items.get(index));

                if (!item.isPresent()) {
                    System.err.println("Clicked an ItemStack that isn't an InventoryMenuItem.");
                    return;
                }
            }

            menu.handler.onClick(menu, index, item);
            event.setCancelled(true);
        }
    }
}
