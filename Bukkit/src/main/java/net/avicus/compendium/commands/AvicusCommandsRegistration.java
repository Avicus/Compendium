package net.avicus.compendium.commands;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.util.ReflectionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import sun.reflect.annotation.AnnotationParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AvicusCommandsRegistration extends CommandsManagerRegistration {

    public AvicusCommandsRegistration(Plugin plugin, CommandsManager commands) {
        super(plugin, commands);
    }

    public AvicusCommandsRegistration(Plugin plugin, CommandExecutor executor, CommandsManager commands) {
        super(plugin, executor, commands);
    }

    // https://gist.github.com/thomasdarimont/7f0a31fcc0a65befef1f
    private static <A extends Annotation> A createAnnotationInstance(Map<String, Object> customValues, Class<A> annotationType) {
        Map<String, Object> values = new HashMap<>();

        //Extract default values from annotation
        for (Method method : annotationType.getDeclaredMethods()) {
            if (values.containsKey(method.getName()))
                continue;

            values.put(method.getName(), method.getDefaultValue());
        }

        //Populate required values
        values.putAll(customValues);

        return (A) AnnotationParser.annotationForMap(annotationType, values);
    }

    /**
     * Unregister commands based on provided class
     * This uses the same logic as the framework uses during registration, so nesting is supported.
     *
     * @param clazz
     * @return
     * @throws Exception
     */
    public boolean unregisterCommands(Class clazz) throws Exception {
        CommandMap map = getCommandMap();

        List<String> toRemove = new ArrayList<>();
        Map<String, Command> knownCommands = ReflectionUtil.getField(map, "knownCommands");
        Set<String> aliases = ReflectionUtil.getField(map, "aliases");

        if (knownCommands == null || aliases == null) {
            return false;
        }

        List<com.sk89q.minecraft.util.commands.Command> commandsInClass = getCommandMethods(clazz, null, null);

        List<String> aliasesToRemove = new ArrayList<>();

        for (com.sk89q.minecraft.util.commands.Command command : commandsInClass) {
            for (int i = 0; i < command.aliases().length; i++) {
                aliasesToRemove.add(command.aliases()[i]);
            }
        }

        for (Iterator<Command> i = knownCommands.values().iterator(); i.hasNext(); ) {
            org.bukkit.command.Command cmd = i.next();
            if (cmd instanceof DynamicPluginCommand && ((DynamicPluginCommand) cmd).getPlugin().equals(plugin) && ((DynamicPluginCommand) cmd).getOwner().equals(executor)) {
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
        toRemove.forEach(knownCommands::remove);
        return true;
    }

    /**
     * Unregister a command
     *
     * @param command
     * @return
     * @throws Exception
     */
    public boolean unregisterCommand(com.sk89q.minecraft.util.commands.Command command) throws Exception {
        CommandMap map = getCommandMap();

        List<String> toRemove = new ArrayList<>();
        Map<String, Command> knownCommands = ReflectionUtil.getField(map, "knownCommands");
        Set<String> aliases = ReflectionUtil.getField(map, "aliases");

        if (knownCommands == null || aliases == null) {
            return false;
        }

        List<String> aliasesToRemove = new ArrayList<>();

        for (int i = 0; i < command.aliases().length; i++) {
            aliasesToRemove.add(command.aliases()[i]);
        }

        for (Iterator<Command> i = knownCommands.values().iterator(); i.hasNext(); ) {
            org.bukkit.command.Command cmd = i.next();
            if (cmd instanceof DynamicPluginCommand && ((DynamicPluginCommand) cmd).getPlugin().equals(plugin) && ((DynamicPluginCommand) cmd).getOwner().equals(executor)) {
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
        toRemove.forEach(knownCommands::remove);
        return true;
    }

    private List<com.sk89q.minecraft.util.commands.Command> getCommandMethods(Class<?> cls, Method parent, Object obj) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<com.sk89q.minecraft.util.commands.Command> registered = new ArrayList<>();

        for (Method method : cls.getMethods()) {
            if (!method.isAnnotationPresent(com.sk89q.minecraft.util.commands.Command.class)) {
                continue;
            }

            if (!(Void.TYPE.equals(method.getReturnType()) ||
                    List.class.isAssignableFrom(method.getReturnType()))) {
                throw new RuntimeException("Command method " + method.getDeclaringClass().getName() + "#" + method.getName() +
                        " must return either void or List<String>");
            }


            com.sk89q.minecraft.util.commands.Command cmd = method.getAnnotation(com.sk89q.minecraft.util.commands.Command.class);

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

    /**
     * Register a new command with the framework based on a method and a supplied command annotation.
     * This will add the command to the framework internal cache and also add it to the {@link CommandMap}.
     *
     * @param method     Method to be executed on command.
     * @param definition Command definition,
     * @return If the command registered successfully.
     * @throws Exception
     */
    public boolean reqisterCommand(Method method, com.sk89q.minecraft.util.commands.Command definition) throws Exception {
        AvicusCommandsManager commandsManager = (AvicusCommandsManager) commands;
        return registerAll(commandsManager.registerCommandAnnotation(method, definition));
    }

    public com.sk89q.minecraft.util.commands.Command newCommand(Map<String, Object> params) {
        return createAnnotationInstance(params, com.sk89q.minecraft.util.commands.Command.class);
    }
}
