package net.avicus.compendium.commands;

import java.lang.reflect.AnnotatedElement;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.util.ReflectionUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.avicus.compendium.AvicusCommandsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

/**
 * A custom implementation of {@link CommandsManagerRegistration} which supports the un-registration
 * of commands.
 */
public class AvicusCommandsRegistration extends CommandsManagerRegistration {

  public AvicusCommandsRegistration(Plugin plugin, AvicusCommandsManager commands) {
    super(plugin, commands);
  }

  public AvicusCommandsRegistration(Plugin plugin, CommandExecutor executor,
      AvicusCommandsManager commands) {
    super(plugin, executor, commands);
  }

  /**
   * Create an instance of an annotation with the supplied arguments and type.
   * <p>
   * SOURCE: https://gist.github.com/thomasdarimont/7f0a31fcc0a65befef1f
   *
   * @param customValues to be supplied to the annotation
   * @param annotationType type of annotation
   * @param <A> type of annotation
   * @return an instance of an annotation with the supplied arguments and type
   */
  private static <A extends Annotation> A createAnnotationInstance(Map<String, Object> customValues,
                                                                   Class<A> annotationType) {
    Map<String, Object> values = new HashMap<>();

    // Extract default values from annotation
    for (Method method : annotationType.getDeclaredMethods()) {
      if (!customValues.containsKey(method.getName())) {
        values.put(method.getName(), method.getDefaultValue());
      }
    }

    // Populate required values
    values.putAll(customValues);

    return createDynamicAnnotation(annotationType, values);
  }

  @SuppressWarnings("unchecked")
  private static <A extends Annotation> A createDynamicAnnotation(Class<A> annotationType, Map<String, Object> values) {
    return (A) Proxy.newProxyInstance(
            annotationType.getClassLoader(),
            new Class[]{annotationType},
            new AnnotationInvocationHandler(annotationType, values)
    );
  }

  private static class AnnotationInvocationHandler implements InvocationHandler {
    private final Class<? extends Annotation> annotationType;
    private final Map<String, Object> values;

    AnnotationInvocationHandler(Class<? extends Annotation> annotationType, Map<String, Object> values) {
      this.annotationType = annotationType;
      this.values = values;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("annotationType") && method.getParameterCount() == 0) {
        return annotationType;
      }
      return values.getOrDefault(method.getName(), method.getDefaultValue());
    }
  }

  /**
   * Unregister commands based on provided class
   * This uses the same logic as the framework uses during registration, so nesting is supported.
   *
   * @param clazz that contains the command annotations
   * @return if the commands were successfully unregistered
   * @throws Exception if un-registration fails
   */
  public boolean unregisterCommands(Class clazz) throws Exception {
    CommandMap map = getCommandMap();

    List<String> toRemove = new ArrayList<>();
    Map<String, Command> knownCommands = ReflectionUtil.getField(map, "knownCommands");
    Set<String> aliases = ReflectionUtil.getField(map, "aliases");

    if (knownCommands == null || aliases == null) {
      return false;
    }

    List<com.sk89q.minecraft.util.commands.Command> commandsInClass = getCommandMethods(clazz, null,
        null);

    List<String> aliasesToRemove = new ArrayList<>();

    for (com.sk89q.minecraft.util.commands.Command command : commandsInClass) {
      for (int i = 0; i < command.aliases().length; i++) {
        aliasesToRemove.add(command.aliases()[i]);
      }
    }

    for (Iterator<Command> i = knownCommands.values().iterator(); i.hasNext(); ) {
      org.bukkit.command.Command cmd = i.next();
      if (cmd instanceof DynamicPluginCommand && ((DynamicPluginCommand) cmd).getPlugin()
          .equals(plugin) && ((DynamicPluginCommand) cmd).getExecutor().equals(executor)) {
        boolean remove = false;
        for (String alias : cmd.getAliases()) {
          org.bukkit.command.Command aliasCmd = knownCommands.get(alias);
          if (cmd.equals(aliasCmd) && aliasesToRemove.contains(alias)) {
            remove = true;
            aliases.remove(alias);
            toRemove.add(alias);
          }
        }

        if (remove) {
          i.remove();
        }
      }
    }
    toRemove.forEach(knownCommands::remove);
    return true;
  }

  /**
   * Unregister a command
   *
   * @param command to unregister
   * @return if the command were successfully unregistered
   * @throws Exception if un-registration fails
   */
  public boolean unregisterCommand(com.sk89q.minecraft.util.commands.Command command)
      throws Exception {
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
      if (cmd instanceof DynamicPluginCommand && ((DynamicPluginCommand) cmd).getPlugin()
          .equals(plugin) && ((DynamicPluginCommand) cmd).getExecutor().equals(executor)) {
        boolean remove = false;
        for (String alias : cmd.getAliases()) {
          org.bukkit.command.Command aliasCmd = knownCommands.get(alias);
          if (cmd.equals(aliasCmd) && aliasesToRemove.contains(alias)) {
            remove = true;
            aliases.remove(alias);
            toRemove.add(alias);
          }
        }

        if (remove) {
          i.remove();
        }
      }
    }
    toRemove.forEach(knownCommands::remove);
    return true;
  }

  /**
   * Get command methods from a class.
   *
   * @param cls to gather methods from
   * @param parent command method
   * @param obj instance of the class
   * @return a list of commands from the class
   * @throws IllegalAccessException if a method cannot be accessed
   * @throws InstantiationException if the class cannot be instantiated
   * @throws InvocationTargetException if invoking a method fails
   */
  private List<com.sk89q.minecraft.util.commands.Command> getCommandMethods(Class<?> cls,
      Method parent, Object obj)
      throws IllegalAccessException, InstantiationException, InvocationTargetException {
    List<com.sk89q.minecraft.util.commands.Command> registered = new ArrayList<>();

    for (Method method : cls.getMethods()) {
      if (!method.isAnnotationPresent(com.sk89q.minecraft.util.commands.Command.class)) {
        continue;
      }

      if (!(Void.TYPE.equals(method.getReturnType()) ||
          List.class.isAssignableFrom(method.getReturnType()))) {
        throw new RuntimeException(
            "Command method " + method.getDeclaringClass().getName() + "#" + method.getName() +
                " must return either void or List<String>");
      }

      com.sk89q.minecraft.util.commands.Command cmd = method
          .getAnnotation(com.sk89q.minecraft.util.commands.Command.class);

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
   * This will add the command to the framework internal cache and also add it to the {@link
   * CommandMap}.
   *
   * @param method Method to be executed on command.
   * @param definition Command definition,
   * @return If the command registered successfully.
   * @throws Exception if registration fails
   */
  public boolean reqisterCommand(Method method,
      com.sk89q.minecraft.util.commands.Command definition) throws Exception {
    AvicusCommandsManager commandsManager = (AvicusCommandsManager) commands;
    return registerAll(commandsManager.registerCommandAnnotation(method, definition));
  }

  /**
   * Helper method to create a new instance of the {@link com.sk89q.minecraft.util.commands.Command}
   * annotation with the supplied arguments.
   *
   * @param params to supply to the annotation
   * @return new instance of the annotation
   */
  public com.sk89q.minecraft.util.commands.Command newCommand(Map<String, Object> params) {
    return createAnnotationInstance(params, com.sk89q.minecraft.util.commands.Command.class);
  }
}
