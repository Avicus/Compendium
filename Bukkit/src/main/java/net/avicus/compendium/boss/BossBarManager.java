package net.avicus.compendium.boss;

import com.keenant.bossy.Bossy;
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
public class BossBarManager implements Listener {

  final Bossy bossy;

  @EventHandler
  public void quit(final PlayerQuitEvent event) {
    this.bossy.hide(event.getPlayer());
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
