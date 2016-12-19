package net.avicus.compendium.settings.command;

import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.settings.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SettingTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length <= 1) {
            for (Setting setting : PlayerSettings.settings()) {
                String name = setting.getName().translate(sender.getLocale()).toPlainText();
                boolean add = args.length == 0 || name.toLowerCase().startsWith(args[0].toLowerCase());
                if (add)
                    list.add(name);
            }
        }

        return list;
    }
}
