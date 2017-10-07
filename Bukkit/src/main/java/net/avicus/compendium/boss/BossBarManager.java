package net.avicus.compendium.boss;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.myles.ViaVersion.api.Via;

/**
 * A boss bar manager.
 */
@RequiredArgsConstructor
public class BossBarManager implements Listener, Runnable {

  /**
   * The boss bar context.
   */
  final LegacyBossBarContext context;
  /**
   * A set of legacy boss bars subscribed to being respawned.
   */
  final Set<LegacyBossBar> legacyUpdateSubscribers = Sets.newHashSet();

  @Override
  public void run() {
    for (LegacyBossBar bar : this.legacyUpdateSubscribers) {
      bar.respawn();
    }
  }

  @EventHandler
  public void quit(final PlayerQuitEvent event) {
    this.context.remove(event.getPlayer());
  }

  /**
   * Create a boss bar.
   *
   * @param player the viewer of the boss bar
   * @return the boss bar
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public BossBar create(@Nonnull final Player player) {
    final int version = Via.getAPI().getPlayerVersion(player);

    if (version >= 4 && version <= 47) // 1.7-1.8.9
    {
      return new LegacyBossBar(this, player);
    } else if (version > 47) // 1.9+
    {
      return new ModernBossBar(player);
    } else {
      throw new RuntimeException(
          "Could not resolve BossBar for protocol " + version + " for " + player.getUniqueId());
    }
  }
}
