package net.avicus.compendium.boss;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.myles.ViaVersion.api.Via;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * A boss bar manager.
 */
@RequiredArgsConstructor
public class BossBarManager implements Listener, Runnable {

    /** The boss bar context. */
    final LegacyBossBarContext context;
    /** A set of legacy boss bars subscribed to being respawned. */
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
        switch (version) {
            case 47: // 1.8 - 1.8.9
                return new LegacyBossBar(this, player);
            case 107: // 1.9
            case 108: // 1.9.1
            case 109: // 1.9.2
            case 110: // 1.9.3, 1.9.4
            case 210: // 1.10 - 1.10.2
            case 315: // 1.11
                return new ModernBossBar(player);
            default:
                throw new RuntimeException("Could not resolve BossBar for protocol " + version + " for " + player.getUniqueId());
        }
    }
}
