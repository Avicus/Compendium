package net.avicus.compendium.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandsManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvicusCommandsManager extends CommandsManager<CommandSender>
{
    @Override
    public boolean hasPermission(CommandSender sender, String perm) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
    }

    /**
     * Registers a {@link Command} annotation with a method.
     * This will cache the definition and method for use to provide {@link com.sk89q.minecraft.util.commands.SuggestionContext}'s and such by the framework.
     * This runs the same code that would run by the internal framework if a class was supplied.
     * The framework will see all of the dynamically registered commands as root-level commands.
     *
     * NOTE: This does not support nesting.
     *
     * @param method Method that the command will execute.
     * @param command definiton of the command,
     * @return the input definition for use in other methods.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<Command> registerCommandAnnotation(Method method, Command command) throws IllegalAccessException, InstantiationException {
        Map<String, Method> map;
        List<Command> registered = new ArrayList<Command>();

        map = commands.get(null);

        if(!(Void.TYPE.equals(method.getReturnType()) ||
                List.class.isAssignableFrom(method.getReturnType()))) {
            throw new RuntimeException("Command method " + method.getDeclaringClass().getName() + "#" + method.getName() +
                    " must return either void or List<String>");
        }

        // Cache the aliases too
        for (String alias : command.aliases()) {
            map.put(alias, method);
        }

        final String commandName = command.aliases()[0];
        final String desc = command.desc();

        final String usage = command.usage();
        if (usage.isEmpty()) {
            descs.put(commandName, desc);
        } else {
            descs.put(commandName, usage + " - " + desc);
        }

        String help = command.help();
        if (help.isEmpty()) {
            help = desc;
        }

        final CharSequence arguments = getArguments(command);
        for (String alias : command.aliases()) {
            final String helpMessage = "/" + alias + " " + arguments + "\n\n" + help;
            final String key = alias.replaceAll("/", "");
            String previous = helpMessages.put(key, helpMessage);

            if (previous != null && !previous.replaceAll("^/[^ ]+ ", "").equals(helpMessage.replaceAll("^/[^ ]+ ", ""))) {
                helpMessages.put(key, previous + "\n\n" + helpMessage);
            }
        }


        // Add the command to the registered command list for return
        registered.add(command);

        return registered;
    }
}
