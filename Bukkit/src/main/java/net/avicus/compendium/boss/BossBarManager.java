package net.avicus.compendium.boss;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.myles.ViaVersion.api.Via;

import javax.annotation.Nonnull;
import java.util.Set;

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
        switch (version) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47: // 1.7 - 1.8.9
                return new LegacyBossBar(this, player);
            case 107: // 1.9
            case 108: // 1.9.1
            case 109: // 1.9.2
            case 110: // 1.9.3, 1.9.4
            case 210: // 1.10 - 1.10.2
            case 315: // 1.11
            case 316: // 1.11.1 - 1.11.12
                return new ModernBossBar(player);
            default:
                throw new RuntimeException("Could not resolve BossBar for protocol " + version + " for " + player.getUniqueId());
        }
    }
}
