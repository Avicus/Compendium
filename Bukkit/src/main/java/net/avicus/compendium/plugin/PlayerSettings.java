package net.avicus.compendium.plugin;

import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.SettingStore;
import net.avicus.compendium.settings.types.BooleanSettingType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A Bukkit player implementation of SettingStore. For multiple plugin usage.
 */
public class PlayerSettings {
    private static SettingStore<Player> store = new SettingStore<>();
    private static List<Setting> settings = new ArrayList<>();

    public static SettingStore<Player> store() {
        return store;
    }

    public static List<Setting> settings() {
        return settings;
    }

    static {
        settings().add(new Setting<>(
                "test",
                new BooleanSettingType(),
                true,
                new UnlocalizedText("Testing"),
                Collections.emptyList(),
                new UnlocalizedText("This is just a test."),
                Optional.empty()
        ));
    }
}
