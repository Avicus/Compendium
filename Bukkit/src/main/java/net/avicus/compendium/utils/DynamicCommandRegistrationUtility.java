package net.avicus.compendium.utils;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import com.sk89q.util.ReflectionUtil;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.util.*;

/*
    Basically a class that does all of the things that command framework does behind the scenes.
    Most code can be found in the core command framework classes.
 */
public class DynamicCommandRegistrationUtility {
    @Getter private final Plugin plugin;
    @Getter private final CommandExecutor executor;
    @Getter private final BukkitCommandsManager manager;

    public DynamicCommandRegistrationUtility(Plugin plugin, CommandExecutor executor, BukkitCommandsManager manager) {
        this.plugin = plugin;
        this.executor = executor;
        this.manager = manager;
    }

    public boolean unregisterCommands(Class clazz) {
        CommandMap map = getCommandMap();

        List<String> toRemove = new ArrayList<String>();
        Map<String, Command> knownCommands = ReflectionUtil.getField(map, "knownCommands");
        Set<String> aliases = ReflectionUtil.getField(map, "aliases");

        if (knownCommands == null || aliases == null) {
            return false;
        }

        List<com.sk89q.minecraft.util.commands.Command> commandsInClass = getManager().registerAndReturn(clazz);

        List<String> aliasesToRemove = new ArrayList<>();
        
        for (com.sk89q.minecraft.util.commands.Command command : commandsInClass) {
            for (int i = 0; i < command.aliases().length; i++) {
                aliasesToRemove.add(command.aliases()[i]);
            }
        }

        for (Iterator<Command> i = knownCommands.values().iterator(); i.hasNext();) {
            Command cmd = i.next();
            if (cmd instanceof DynamicPluginCommand && ((DynamicPluginCommand) cmd).getPlugin().equals(plugin) && ((DynamicPluginCommand) cmd).getOwner().equals(getExecutor())) {
                boolean remove = false;
                for (String alias : cmd.getAliases()) {
                    org.bukkit.command.Command aliasCmd = knownCommands.get(alias);
                    if (cmd.equals(aliasCmd) && aliasesToRemove.contains(alias)) {
                        remove = true;
                        aliases.remove(alias);
                        toRemove.add(alias);
                    }
                }

                if (remove)
                    i.remove();
            }
        }
        for (String string : toRemove) {
            knownCommands.remove(string);
        }
        return true;
    }

    public CommandMap getCommandMap() {
        return ReflectionUtil.getField(plugin.getServer().getPluginManager(), "commandMap");
    }

}
