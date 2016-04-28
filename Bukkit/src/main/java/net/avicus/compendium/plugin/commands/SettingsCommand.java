package net.avicus.compendium.plugin.commands;

import net.avicus.compendium.Paginator;
import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.settings.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class SettingsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 2)
            return false;

        Locale locale = Locales.getLocale(sender);

        List<Setting> list = new ArrayList<>(PlayerSettings.settings());

        int page = 0;
        if (args.length > 0) {
            String query = args[0];

            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // Not a number, maybe a query?

                Iterator<Setting> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Setting next = iterator.next();
                    String name = next.getName().translate(locale).toPlainText();
                    if (!name.toLowerCase().contains(query.toLowerCase()))
                        iterator.remove();
                }

                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (Exception e1) {
                        return false;
                    }
                }
            }
        }

        Paginator<Setting> paginator = new Paginator<>(list, 5);
        paginator.sort(new Comparator<Setting>() {
            @Override
            public int compare(Setting o1, Setting o2) {
                String n1 = o1.getName().translate(locale).toPlainText();
                String n2 = o2.getName().translate(locale).toPlainText();
                return n1.compareTo(n2);
            }
        });

        if (!paginator.hasPage(page))
            return false;

        for (Setting setting : paginator.getList()) {
            String name = setting.getName().translate(locale).toPlainText();
            sender.sendMessage(name);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
