package net.avicus.compendium.plugin;

import net.avicus.compendium.plugin.commands.SetCommand;
import net.avicus.compendium.plugin.commands.SettingCommand;
import net.avicus.compendium.plugin.commands.SettingsCommand;
import net.avicus.compendium.plugin.commands.ToggleCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CompendiumPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("set").setExecutor(new SetCommand());
        getCommand("setting").setExecutor(new SettingCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("toggle").setExecutor(new ToggleCommand());
    }
}
