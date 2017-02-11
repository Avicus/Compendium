package net.avicus.compendium.plugin;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandNumberFormatException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import lombok.Getter;
import net.avicus.compendium.boss.BossBarManager;
import net.avicus.compendium.boss.LegacyBossBarContext;
import net.avicus.compendium.commands.AvicusCommandsManager;
import net.avicus.compendium.commands.AvicusCommandsRegistration;
import net.avicus.compendium.commands.exception.AbstractTranslatableCommandException;
import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.countdown.CountdownCommands;
import net.avicus.compendium.menu.inventory.InventoryListener;
import net.avicus.compendium.settings.command.SettingCommands;
import net.avicus.compendium.settings.command.SettingTabCompleter;
import net.avicus.compendium.countdown.CountdownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompendiumPlugin extends JavaPlugin {
    @Getter private static CompendiumPlugin instance;
    private AvicusCommandsManager commandManager;
    private static LocaleBundle bundle;

    @Getter private BossBarManager bossBarManager;
    @Getter private CountdownManager countdownManager;

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

        this.registerCommands();

        final PluginManager pm = this.getServer().getPluginManager();
        final BukkitScheduler scheduler = this.getServer().getScheduler();

        final LegacyBossBarContext legacyContext = new LegacyBossBarContext();
        scheduler.runTaskTimer(this, legacyContext, 0, 5 * 20);
        this.bossBarManager = new BossBarManager(legacyContext);
        pm.registerEvents(this.bossBarManager, this);
        scheduler.runTaskTimer(this, this.bossBarManager, 0, 5);
        this.countdownManager = new CountdownManager();
        pm.registerEvents(this.countdownManager, this);
        pm.registerEvents(new InventoryListener(), this);
    }

    private void registerCommands() {
        this.commandManager = new AvicusCommandsManager();
        AvicusCommandsRegistration registry = new AvicusCommandsRegistration(this, this.commandManager);
        registry.register(SettingCommands.class);
        registry.register(CountdownCommands.class);

        final SettingTabCompleter completer = new SettingTabCompleter();
        this.getCommand("set").setTabCompleter(completer);
        this.getCommand("setting").setTabCompleter(completer);
        this.getCommand("settings").setTabCompleter(completer);
        this.getCommand("toggle").setTabCompleter(completer);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        try {
            this.commandManager.execute(command.getName(), args, sender, sender);
        } catch (AbstractTranslatableCommandException e) {
            sender.sendMessage(AbstractTranslatableCommandException.format(e));
        } catch (CommandNumberFormatException e) {
            sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERRORS_COMMAND_NUMBER_EXPECTED, new UnlocalizedText(e.getActualText())));
        } catch (CommandPermissionsException e) {
            sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERRORS_COMMAND_NO_PERMISSION));
        } catch (CommandUsageException e) {
            sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERRORS_COMMAND_INVALID_USAGE, new UnlocalizedText(e.getUsage())));
        } catch (WrappedCommandException e) {
            sender.sendMessage(e.getMessage());
        } catch (CommandException e) {
            sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERRORS_COMMAND_INTERNAL_ERROR));
            e.printStackTrace();
        }

        return true;
    }

    private void locales() throws JDOMException, IOException {
        List<LocaleStrings> strings = new ArrayList<>();
        strings.add(LocaleStrings.fromXml(getResource("locales/en.xml")));
        // todo: more locales
        bundle = new LocaleBundle(strings);
    }
}
