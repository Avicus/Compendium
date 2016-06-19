package net.avicus.compendium.plugin.commands;

import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.plugin.PlayersBase;
import net.avicus.compendium.settings.Setting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Optional;

public class ToggleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1)
            return false;

        if (!(sender instanceof Player)) {
            PlayersBase.message(sender, Messages.ERRORS_NOT_PLAYER.with(ChatColor.RED));
            return true;
        }

        Locale locale = Locales.getLocale(sender);
        String query = args[0];

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            PlayersBase.message(sender, Messages.ERRORS_SETTINGS_HELP.with(ChatColor.RED));
            return true;
        }

        Setting<Object> setting = search.get();

        Optional<Object> result = PlayerSettings.store().toggle((Player) sender, setting);

        if (result.isPresent()) {
            Localizable name = setting.getName().duplicate();
            Localizable value = new UnlocalizedText(setting.getType().value(result.get()).serialize());

            PlayersBase.message(sender, Messages.GENERIC_SETTING_SET.with(ChatColor.GOLD, name, value));
        }
        else {
            PlayersBase.message(sender, Messages.ERRORS_NOT_TOGGLE.with(ChatColor.RED));
        }

        return true;
    }
}
