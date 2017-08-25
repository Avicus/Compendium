package net.avicus.compendium.plugin;

import net.avicus.compendium.utils.Task;
import org.bukkit.plugin.Plugin;

public abstract class CompendiumTask extends Task {

  @Override
  public Plugin getPlugin() {
    return CompendiumPlugin.getInstance();
  }
}
