package net.avicus.compendium.plugin;

import com.keenant.bossy.Bossy;
import lombok.Getter;
import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.plugin.commands.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompendiumPlugin extends JavaPlugin {
    @Getter private static CompendiumPlugin instance;
    private static LocaleBundle bundle;

    @Getter private static Bossy bossy;

    public static LocaleBundle getLocaleBundle() {
        return bundle;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            locales();
        } catch (Exception e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }

        getCommand("set").setExecutor(new SetCommand());
        getCommand("set").setTabCompleter(new SettingTabCompleter());
        getCommand("setting").setExecutor(new SettingCommand());
        getCommand("setting").setTabCompleter(new SettingTabCompleter());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("settings").setTabCompleter(new SettingTabCompleter());
        getCommand("toggle").setExecutor(new ToggleCommand());
        getCommand("toggle").setTabCompleter(new SettingTabCompleter());

        bossy = new Bossy(this);;
    }

    private void locales() throws JDOMException, IOException {
        List<LocaleStrings> strings = new ArrayList<>();
        strings.add(LocaleStrings.fromXml(getResource("locales/en.xml")));
        // todo: more locales
        bundle = new LocaleBundle(strings);
    }
}
