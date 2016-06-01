package net.avicus.compendium.inventory;

import org.bukkit.Material;

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
}
