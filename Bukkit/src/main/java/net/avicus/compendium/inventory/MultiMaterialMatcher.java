package net.avicus.compendium.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.List;

public class MultiMaterialMatcher implements MaterialMatcher {
    @Getter private final List<SingleMaterialMatcher> matchers;

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

    public void replaceMaterial(MultiMaterialMatcher find, SingleMaterialMatcher replace) {
        for (SingleMaterialMatcher matcher : this.matchers) {
            for (SingleMaterialMatcher findMatcher : find.getMatchers()) {
                if (findMatcher.matches(matcher.getMaterial(), matcher.getData().orElse((byte) 0))) {
                    this.matchers.remove(matcher);
                    this.matchers.add(new SingleMaterialMatcher(replace.getMaterial(), replace.getData()));
                }
            }
        }
    }
}
