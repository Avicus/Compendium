package net.avicus.compendium.settings;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
}
