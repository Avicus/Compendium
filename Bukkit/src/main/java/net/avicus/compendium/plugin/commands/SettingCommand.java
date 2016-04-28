package net.avicus.compendium.plugin.commands;

import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedFormat;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.plugin.Players;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.SettingValueToggleable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class SettingCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1)
            return false;

        if (!(sender instanceof Player)) {
            Players.message(sender, Messages.ERRORS_NOT_PLAYER.with(ChatColor.RED));
            return true;
        }

        Locale locale = Locales.getLocale(sender);
        String query = args[0];

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            Players.message(sender, Messages.ERRORS_SETTINGS_HELP.with(ChatColor.RED));
            return false;
        }

        Setting<Object> setting = search.get();

        // Header
        UnlocalizedText line = new UnlocalizedText("--------------", TextStyle.ofColor(ChatColor.RED).strike());
        UnlocalizedFormat header = new UnlocalizedFormat("{0} {1} {2}");
        Localizable name = setting.getName().duplicate();
        Players.message(sender, header.with(ChatColor.YELLOW, line, name, line));

        // Summary
        Localizable summary = setting.getName().duplicate();
        summary.style().color(ChatColor.WHITE);
        Players.message(sender, Messages.GENERIC_SUMMARY.with(ChatColor.YELLOW, summary));

        if (setting.getDescription().isPresent()) {
            // Description
            Localizable desc = setting.getDescription().get().duplicate();
            desc.style().color(ChatColor.WHITE);
            Players.message(sender, Messages.GENERIC_DESCRIPTION.with(ChatColor.YELLOW, desc));
        }

        // Current value
        Object currentRaw = PlayerSettings.store().get((Player) sender, setting);
        String current = setting.getType().value(currentRaw).serialize();
        Localizable currentText = new UnlocalizedText(current, ChatColor.WHITE);

        Players.message(sender, Messages.GENERIC_CURRENT.with(ChatColor.YELLOW, currentText));

        // Default value
        Object defaultRaw = setting.getDefaultValue();
        String def = setting.getType().value(defaultRaw).serialize();
        Localizable defText = new UnlocalizedText(def, ChatColor.WHITE);

        Players.message(sender, Messages.GENERIC_DEFAULT.with(ChatColor.YELLOW, defText));


        if (setting.getType().value(setting.getDefaultValue()) instanceof SettingValueToggleable) {
            Localizable toggle = Messages.GENERIC_TOGGLE.with(ChatColor.YELLOW);
            toggle.style().underlined();
            toggle.style().click(new ClickEvent(Action.RUN_COMMAND, "/toggle " + name.translate(locale).toPlainText()));

            Players.message(sender, toggle);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        Locale locale = Locales.getLocale(sender);

        List<String> list = new ArrayList<>();

        if (args.length <= 1) {
            for (Setting setting : PlayerSettings.settings()) {
                String name = setting.getName().translate(locale).toPlainText();
                boolean add = args.length == 0 || name.toLowerCase().startsWith(args[0].toLowerCase());
                if (add)
                    list.add(name);
            }
        }

        return list;
    }
}
