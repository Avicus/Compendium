package net.avicus.compendium.settings.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.avicus.compendium.Paginator;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.commands.exception.InvalidPaginationPageException;
import net.avicus.compendium.commands.exception.TranslatableCommandErrorException;
import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.LocalizedNumber;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.plugin.PlayersBase;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.SettingValue;
import net.avicus.compendium.settings.SettingValueToggleable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class SettingCommands {

    @Command(aliases = {"set"}, desc = "Set a setting to a specific value.", usage = "<name> <value>", min = 2)
    public static void set(CommandContext args, CommandSender sender) throws TranslatableCommandErrorException {
        Locale locale = Locales.getLocale(sender);

        if (!(sender instanceof Player)) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_NOT_PLAYER);
        }

        Player player = (Player) sender;

        String query = args.getString(0);

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_SETTINGS_HELP);
        }

        Setting<Object> setting = search.get();

        Optional<SettingValue> value = (Optional<SettingValue>) setting.getType().parse(args.getString(1));

        if (!value.isPresent()) {
            PlayersBase.message(sender, Messages.ERRORS_INVALID_VALUE.with(ChatColor.RED));
            return;
        }

        PlayerSettings.store().set(player.getUniqueId(), setting, value.get().raw());

        Localizable name = setting.getName().duplicate();
        Localizable set = new UnlocalizedText(value.get().serialize());
        PlayersBase.message(player, Messages.GENERIC_SETTING_SET.with(ChatColor.GOLD, name, set));
    }

    @Command(aliases = {"setting"}, desc = "See a setting's value and information.", min = 1, usage = "<name>")
    public static void setting(CommandContext args, CommandSender sender) throws TranslatableCommandErrorException {
        if (!(sender instanceof Player)) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_NOT_PLAYER);
        }

        Locale locale = Locales.getLocale(sender);
        String query = args.getString(0);

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_SETTINGS_HELP);
        }

        Setting<Object> setting = search.get();

        // Header
        UnlocalizedText line = new UnlocalizedText("--------------", TextStyle.ofColor(ChatColor.RED).strike());
        UnlocalizedFormat header = new UnlocalizedFormat("{0} {1} {2}");
        Localizable name = setting.getName().duplicate();
        PlayersBase.message(sender, header.with(ChatColor.YELLOW, line, name, line));

        // Summary
        Localizable summary = setting.getSummary().duplicate();
        summary.style().color(ChatColor.WHITE);
        PlayersBase.message(sender, Messages.GENERIC_SUMMARY.with(ChatColor.YELLOW, summary));

        if (setting.getDescription().isPresent()) {
            // Description
            Localizable desc = setting.getDescription().get().duplicate();
            desc.style().color(ChatColor.WHITE);
            PlayersBase.message(sender, Messages.GENERIC_DESCRIPTION.with(ChatColor.YELLOW, desc));
        }

        // Current value
        Object currentRaw = PlayerSettings.store().get(((Player) sender).getUniqueId(), setting);
        String current = setting.getType().value(currentRaw).serialize();
        Localizable currentText = new UnlocalizedText(current, ChatColor.WHITE);

        PlayersBase.message(sender, Messages.GENERIC_CURRENT.with(ChatColor.YELLOW, currentText));

        // Default value
        Object defaultRaw = setting.getDefaultValue();
        String def = setting.getType().value(defaultRaw).serialize();
        Localizable defText = new UnlocalizedText(def, ChatColor.WHITE);

        PlayersBase.message(sender, Messages.GENERIC_DEFAULT.with(ChatColor.YELLOW, defText));


        if (setting.getType().value(setting.getDefaultValue()) instanceof SettingValueToggleable) {
            Localizable toggle = Messages.GENERIC_TOGGLE.with(ChatColor.YELLOW);
            toggle.style().italic();
            toggle.style().click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/toggle " + name.translate(locale).toPlainText()));

            PlayersBase.message(sender, toggle);
        }
    }

    @Command(aliases = {"settings"}, desc = "List available settings.", max = 0, usage = "[page/query]")
    public static void settings(CommandContext args, CommandSender sender) throws TranslatableCommandErrorException {
        if (!(sender instanceof Player)) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_NOT_PLAYER);
        }

        Locale locale = Locales.getLocale(sender);

        List<Setting> list = new ArrayList<>(PlayerSettings.settings());

        int page = 1;
        if (args.argsLength() > 0) {
            String query = args.getString(0);

            try {
                page = Integer.parseInt(query);
            } catch (Exception e) {
                // Not a number, maybe a query?

                Iterator<Setting> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Setting next = iterator.next();
                    String name = next.getName().translate(locale).toPlainText();
                    if (!name.toLowerCase().contains(query.toLowerCase()))
                        iterator.remove();
                }

                if (args.argsLength() > 1) {
                    try {
                        page = Integer.parseInt(args.getString(1));
                    } catch (Exception e1) {
                        return;
                    }
                }
            }
        }

        // page index = page - 1
        page--;

        list.sort((o1, o2) -> {
            String n1 = o1.getName().translate(locale).toPlainText();
            String n2 = o2.getName().translate(locale).toPlainText();
            return n1.compareTo(n2);
        });

        Paginator<Setting> paginator = new Paginator<>(list, 5);

        if (!paginator.hasPage(page)) {
            throw new InvalidPaginationPageException(paginator);
        }

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

        for (Setting setting : paginator.getCollection()) {
            Localizable name = setting.getName().duplicate();
            name.style().click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setting " + name.translate(locale).toPlainText()));
            name.style().hover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, clickMe));
            name.style().italic();
            name.style().color(ChatColor.YELLOW);

            Localizable summary = setting.getSummary().duplicate();

            PlayersBase.message(sender, format.with(ChatColor.WHITE, name, summary));
        }
    }

    @Command(aliases = {"toggle"}, desc = "Toggle a setting between values.", max = 1)
    public static void toggle(CommandContext args, CommandSender sender) throws TranslatableCommandErrorException {
        if (!(sender instanceof Player)) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_NOT_PLAYER);
        }

        Locale locale = Locales.getLocale(sender);
        String query = args.getString(0);

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            throw new TranslatableCommandErrorException(Messages.ERRORS_SETTINGS_HELP);
        }

        Setting<Object> setting = search.get();

        Optional<Object> result = PlayerSettings.store().toggle(((Player) sender).getUniqueId(), setting);

        if (result.isPresent()) {
            Localizable name = setting.getName().duplicate();
            Localizable value = new UnlocalizedText(setting.getType().value(result.get()).serialize());

            PlayersBase.message(sender, Messages.GENERIC_SETTING_SET.with(ChatColor.GOLD, name, value));
        }
        else {
            PlayersBase.message(sender, Messages.ERRORS_NOT_TOGGLE.with(ChatColor.RED));
        }
    }
}