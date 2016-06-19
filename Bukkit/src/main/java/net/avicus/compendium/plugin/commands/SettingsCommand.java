package net.avicus.compendium.plugin.commands;

import net.avicus.compendium.Paginator;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.plugin.PlayersBase;
import net.avicus.compendium.settings.Setting;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 2)
            return false;

        Locale locale = Locales.getLocale(sender);

        List<Setting> list = new ArrayList<>(PlayerSettings.settings());

        int page = 1;
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

        // page index = page - 1
        page--;

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

        // Page Header
        UnlocalizedText line = new UnlocalizedText("--------------", TextStyle.ofColor(ChatColor.RED).strike());
        UnlocalizedFormat header = new UnlocalizedFormat("{0} {1} ({2}/{3}) {4}");
        LocalizedNumber pageNumber = new LocalizedNumber(page + 1);
        LocalizedNumber pagesNumber = new LocalizedNumber(paginator.getPageCount());
        Localizable title = Messages.GENERIC_SETTINGS.with(ChatColor.YELLOW);
        PlayersBase.message(sender, header.with(line, title, pageNumber, pagesNumber, line));

        // Setting Format
        UnlocalizedFormat format = new UnlocalizedFormat("{0}: {1}");

        // Click me!
        TextComponent[] clickMe = new TextComponent[] {Messages.GENERIC_CLICK_ME.with(ChatColor.WHITE).translate(locale)};

        for (Setting setting : paginator.getList()) {
            Localizable name = setting.getName().duplicate();
            name.style().click(new ClickEvent(Action.RUN_COMMAND, "/setting " + name.translate(locale).toPlainText()));
            name.style().hover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, clickMe));
            name.style().italic();
            name.style().color(ChatColor.YELLOW);

            Localizable summary = setting.getSummary().duplicate();

            PlayersBase.message(sender, format.with(ChatColor.WHITE, name, summary));
        }

        return true;
    }
}
