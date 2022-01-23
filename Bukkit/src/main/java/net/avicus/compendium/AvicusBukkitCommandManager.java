package net.avicus.compendium;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class AvicusBukkitCommandManager <T extends CommandSender> extends AvicusCommandsManager<T> {

  @Override
  public boolean hasPermission(T sender, String perm) {
    return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
  }
}
