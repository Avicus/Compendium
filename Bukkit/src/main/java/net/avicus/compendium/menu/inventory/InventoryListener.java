package net.avicus.compendium.menu.inventory;

import net.avicus.compendium.plugin.CompendiumPlugin;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class InventoryListener implements Listener {

    @EventHandler
    public void inventoryClose(final InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof InventoryMenuAdapter)) {
            return;
        }

        final InventoryMenu menu = ((InventoryMenuAdapter) holder).getMenu();
        menu.close();

        new BukkitRunnable() {
            @Override
            public void run() {
                menu.onExit();
            }
        }.runTask(CompendiumPlugin.getInstance());
    }

    @EventHandler
    public void playerQuit(final PlayerQuitEvent event) {
        final InventoryHolder holder = event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        if (!(holder instanceof InventoryMenuAdapter)) {
            return;
        }

        final InventoryMenu menu = ((InventoryMenuAdapter) holder).getMenu();
        menu.close();
        menu.onExit();
    }

    @EventHandler
    public void inventoryClick(final InventoryClickEvent event) {
        final HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) {
            return;
        }

        if (event.getClickedInventory() == human.getInventory()) {
            return;
        }

        final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof InventoryMenuAdapter)) {
            return;
        }

        final InventoryMenu menu = ((InventoryMenuAdapter) holder).getMenu();

        int index = event.getSlot();
        ItemStack clicked = event.getCurrentItem();

        Optional<InventoryMenuItem> item = Optional.empty();

        if (clicked != null && clicked.getType() != Material.AIR) {
            item = Optional.ofNullable(menu.getItemMap().get(index));

            if (!item.isPresent()) {
                System.err.println("Clicked an ItemStack that isn't an InventoryMenuItem.");
                return;
            }
        }

        menu.getHandler().onClick(menu, index, item, event.getClick());
        event.setCancelled(true);
    }
}
