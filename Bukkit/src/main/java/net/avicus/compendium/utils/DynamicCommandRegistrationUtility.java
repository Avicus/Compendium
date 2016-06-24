package net.avicus.compendium.utils;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.util.ReflectionUtil;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public boolean unregisterCommands(Class clazz) throws Exception {
        CommandMap map = getCommandMap();

        List<String> toRemove = new ArrayList<String>();
        Map<String, org.bukkit.command.Command> knownCommands = ReflectionUtil.getField(map, "knownCommands");
        Set<String> aliases = ReflectionUtil.getField(map, "aliases");

        if (knownCommands == null || aliases == null) {
            return false;
        }

        List<Command> commandsInClass = getCommandMethods(clazz, null, null);

        List<String> aliasesToRemove = new ArrayList<>();
        
        for (Command command : commandsInClass) {
            for (int i = 0; i < command.aliases().length; i++) {
                aliasesToRemove.add(command.aliases()[i]);
            }
        }

        for (Iterator<org.bukkit.command.Command> i = knownCommands.values().iterator(); i.hasNext();) {
            org.bukkit.command.Command cmd = i.next();
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

    // Direct reproduction of the code command framework uses to gather command methods.
    private List<Command> getCommandMethods(Class<?> cls, Method parent, Object obj) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Command> registered = new ArrayList<Command>();

        for (Method method : cls.getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            if(!(Void.TYPE.equals(method.getReturnType()) ||
                    List.class.isAssignableFrom(method.getReturnType()))) {
                throw new RuntimeException("Command method " + method.getDeclaringClass().getName() + "#" + method.getName() +
                        " must return either void or List<String>");
            }


            Command cmd = method.getAnnotation(Command.class);

            // Add the command to the registered command list for return
            registered.add(cmd);

            // Look for nested commands -- if there are any, those have
            // to be cached too so that they can be quickly looked
            // up when processing commands
            if (method.isAnnotationPresent(NestedCommand.class)) {
                NestedCommand nestedCmd = method.getAnnotation(NestedCommand.class);

                for (Class<?> nestedCls : nestedCmd.value()) {
                    getCommandMethods(nestedCls, method, null);
                }
            }
        }

        if (cls.getSuperclass() != null) {
            getCommandMethods(cls.getSuperclass(), parent, obj);
        }

        return registered;
    }

}
