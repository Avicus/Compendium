package net.avicus.compendium.inventory;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public interface MaterialMatcher {
    boolean matches(Material material, byte data);

    default boolean matches(MaterialData materialData) {
        return matches(materialData.getItemType(), materialData.getData());
    }

    default boolean matches(BlockState state) {
        return matches(state.getData());
    }
}
