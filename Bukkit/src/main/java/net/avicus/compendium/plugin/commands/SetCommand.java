package net.avicus.compendium.plugin.commands;

import net.avicus.compendium.locale.Locales;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.plugin.Messages;
import net.avicus.compendium.plugin.PlayerSettings;
import net.avicus.compendium.plugin.PlayersBase;
import net.avicus.compendium.settings.Setting;
import net.avicus.compendium.settings.SettingValue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Optional;

public class SetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 2)
            return false;

        Locale locale = Locales.getLocale(sender);

        if (!(sender instanceof Player)) {
            PlayersBase.message(sender, Messages.ERRORS_NOT_PLAYER.with(ChatColor.RED));
            return true;
        }

        Player player = (Player) sender;

        String query = args[0];

        Optional<Setting> search = Setting.search(locale, query, PlayerSettings.settings());

        if (!search.isPresent()) {
            PlayersBase.message(sender, Messages.ERRORS_SETTINGS_HELP.with(ChatColor.RED));
            return true;
        }

        Setting<Object> setting = search.get();

        Optional<SettingValue> value = (Optional<SettingValue>) setting.getType().parse(args[1]);

        if (!value.isPresent()) {
            PlayersBase.message(sender, Messages.ERRORS_INVALID_VALUE.with(ChatColor.RED));
            return true;
        }

        PlayerSettings.store().set(player.getUniqueId(), setting, value.get().raw());

        Localizable name = setting.getName().duplicate();
        Localizable set = new UnlocalizedText(value.get().serialize());
        PlayersBase.message(player, Messages.GENERIC_SETTING_SET.with(ChatColor.GOLD, name, set));
        return true;
    }
}
