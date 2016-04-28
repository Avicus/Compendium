package net.avicus.compendium.plugin;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.plugin.commands.SetCommand;
import net.avicus.compendium.plugin.commands.SettingCommand;
import net.avicus.compendium.plugin.commands.SettingsCommand;
import net.avicus.compendium.plugin.commands.ToggleCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompendiumPlugin extends JavaPlugin {
    private static LocaleBundle bundle;

    public static LocaleBundle getLocaleBundle() {
        return bundle;
    }

    @Override
    public void onEnable() {
        try {
            locales();
        } catch (Exception e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }

        getCommand("set").setExecutor(new SetCommand());
        getCommand("set").setTabCompleter(new SetCommand());
        getCommand("setting").setExecutor(new SettingCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("toggle").setExecutor(new ToggleCommand());
    }

    private void locales() throws JDOMException, IOException {
        List<LocaleStrings> strings = new ArrayList<>();
        strings.add(LocaleStrings.fromXml(getResource("locales/en.xml")));
        // todo: more locales
        this.bundle = new LocaleBundle(strings);
    }
}
