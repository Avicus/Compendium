package net.avicus.compendium.menu;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class InventoryMenu implements Menu<InventoryMenuView> {
    private final List<InventoryMenuView> opened;

    public InventoryMenu() {
        this.opened = new ArrayList<>();
    }

    public abstract int getSize();

    @Override
    public Optional<InventoryMenuView> getView(Player player) {
        for (InventoryMenuView context : this.opened)
            if (context.getPlayer().equals(player))
                return Optional.of(context);
        return Optional.empty();
    }

    @Override
    public InventoryMenuView open(Player player) {
        InventoryMenuView view = new InventoryMenuView(this, player);
        this.opened.add(view);
        view.open();
        return view;
    }

    @Override
    public Optional<InventoryMenuView> close(Player player) {
        Optional<InventoryMenuView> view = getView(player);
        if (view.isPresent()) {
            this.opened.remove(view.get());
            view.get().close();
        }
        return view;
    }
}
