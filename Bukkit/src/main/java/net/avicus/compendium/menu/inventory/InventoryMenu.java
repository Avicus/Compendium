package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class InventoryMenu implements Menu {
    private final Plugin plugin;
    private final Player player;
    private final InventoryIndexer indexer;
    private final InventoryHandler handler;
    private final Map<Integer, InventoryMenuItem> items;
    private final int rows;
    private final Listener listener;
    private Inventory inventory;

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows) {
        this(plugin, player, items, rows, new DefaultInventoryIndexer(), new DefaultInventoryHandler());
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryHandler handler) {
        this(plugin, player, items, rows, new DefaultInventoryIndexer(), handler);
    }

    public InventoryMenu(Plugin plugin, Player player, List<InventoryMenuItem> items, int rows, InventoryIndexer indexer) {
        this(plugin, player, items, rows, indexer, new DefaultInventoryHandler());
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
            boolean updateRequested = (oldItem != null && oldItem.update()) || (newItem != null && newItem.update());

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

    private class InventoryMenuListener implements Listener {
        @EventHandler
        public void onInventoryClose(InventoryOpenEvent event) {
            if (!event.getInventory().equals(inventory))
                close();
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            close();
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            InventoryMenu menu = InventoryMenu.this;

            int index = event.getSlot();
            ItemStack clicked = event.getCurrentItem();

            Optional<InventoryMenuItem> item = Optional.empty();

            if (clicked != null) {
                item = Optional.ofNullable(menu.items.get(index));

                if (!item.isPresent()) {
                    System.err.println("Clicked itemstack that doesn't have InventoryMenuItem.");
                    return;
                }
            }

            menu.handler.onClick(menu, index, item);

            event.setCancelled(true);
        }
    }
}
