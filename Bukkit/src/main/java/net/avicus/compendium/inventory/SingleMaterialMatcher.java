package net.avicus.compendium.inventory;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import java.util.Optional;

@ToString
public class SingleMaterialMatcher implements MaterialMatcher {
    @Getter final Material material;
    @Getter final Optional<Byte> data;

    public SingleMaterialMatcher(Material material) {
        this(material, Optional.empty());
    }

    public SingleMaterialMatcher(Material material, byte data) {
        this(material, Optional.of(data));
    }

    public SingleMaterialMatcher(BlockState state) {
        this(state.getType(), state.getRawData());
    }

    public SingleMaterialMatcher(Material material, Optional<Byte> data) {
        this.material = material;
        this.data = data;
    }

    public MaterialData toMaterialData() {
        return new MaterialData(this.material, this.data.orElse((byte) 0));
    }

    public boolean matches(Material material, byte data) {
        if (this.material != material)
            return false;
        return !(this.data.isPresent() && this.data.get() != data);
    }

    public boolean isDataRelevant() {
        return this.data.isPresent();
    }
}