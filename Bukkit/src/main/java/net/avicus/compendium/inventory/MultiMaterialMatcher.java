package net.avicus.compendium.inventory;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.List;

public class MultiMaterialMatcher implements MaterialMatcher {
    private final List<SingleMaterialMatcher> matchers;

    public MultiMaterialMatcher(List<SingleMaterialMatcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(Material material, byte data) {
        for (SingleMaterialMatcher matcher : this.matchers)
            if (matcher.matches(material, data))
                return true;
        return false;
    }

    public void replaceMaterial(Material material, Byte data) {
        if (!this.matches(material, data))
            return;

        for (SingleMaterialMatcher matcher : this.matchers) {
            if (matcher.matches(material, data)) {
                this.matchers.remove(matcher);
                this.matchers.add(new SingleMaterialMatcher(material, data));
            }
        }
    }

    public void replaceMaterial(MaterialData material) {
        for (SingleMaterialMatcher matcher : this.matchers) {
            if (matcher.matches(material)) {
                this.matchers.remove(matcher);
                this.matchers.add(new SingleMaterialMatcher(material.getItemType()));
            }
        }
    }
}
