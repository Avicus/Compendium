package net.avicus.compendium;

import net.avicus.minecraft.api.command.ConsoleCommandSender;
import net.md_5.bungee.api.CommandSender;

public class AvicusBungeeCommandManager<T extends CommandSender> extends AvicusCommandsManager<T> {

  @Override
  public boolean hasPermission(T sender, String perm) {
    return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
  }
}
