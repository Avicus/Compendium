package net.avicus.compendium.boss;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A legacy boss bar.
 * <p>
 * <p>A legacy boss bar is used for 1.8 clients.</p>
 */
public class LegacyBossBar extends BossBar {

  /**
   * The boss bar manager.
   */
  private BossBarManager manager;
  /**
   * The player who is viewing this boss bar.
   */
  private Player player;

  /**
   * Constructs a new legacy boss bar.
   *
   * @param manager the boss bar manager
   * @param player the player who is viewing this boss bar
   */
  LegacyBossBar(BossBarManager manager, Player player) {
    this.manager = manager;
    this.player = player;
  }

  @Override
  public void destroy() {
    this.despawn();
    this.manager = null;
    this.player = null;
  }

  @Override
  public LegacyBossBar setName(BaseComponent[] name) {
    if (name != this.name) {
      super.setName(name);
      try {
        this.manager.bossy.setText(this.player, BaseComponent.toLegacyText(this.name));
      } catch (Exception e) {
        Bukkit.getLogger().warning("Failed to set boss bar name for " + this.player.getName());
      }
    }

    return this;
  }

  @Override
  public LegacyBossBar setPercent(float percent) {
    if (percent != this.percent) {
      super.setPercent(percent);
      this.manager.bossy.setPercent(this.player, this.percent);
    }

    return this;
  }

  @Override
  public LegacyBossBar setColor(BossBarColor color) {
    // not supported
    super.setColor(color);
    return this;
  }

  @Override
  public LegacyBossBar setOverlay(BossBarOverlay overlay) {
    // not supported
    super.setOverlay(overlay);
    return this;
  }

  @Override
  public LegacyBossBar setDarkenSky(boolean darkenSky) {
    // not supported
    super.setDarkenSky(darkenSky);
    return this;
  }

  @Override
  public LegacyBossBar setPlayEndBossMusic(boolean playEndBossMusic) {
    // not supported
    super.setPlayEndBossMusic(playEndBossMusic);
    return this;
  }

  @Override
  public LegacyBossBar setCreateFog(boolean createFog) {
    // not supported
    super.setCreateFog(createFog);
    return this;
  }

  @Override
  public LegacyBossBar setVisible(boolean visible) {
    if (visible != this.visible) {
      super.setVisible(visible);

      if (visible) {
        this.spawn();
      } else {
        this.despawn();
      }
    }

    return this;
  }

  /**
   * Spawn a fake entity on the client used to render the boss bar.
   */
  void spawn() {
    this.manager.bossy.show(this.player);
  }

  /**
   * Removes the fake entity from the client.
   */
  void despawn() {
    this.manager.bossy.hide(this.player);
  }
}
