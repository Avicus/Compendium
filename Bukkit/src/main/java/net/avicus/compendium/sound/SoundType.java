package net.avicus.compendium.sound;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum SoundType {

  NONE(null),

  // Generic
  DING(Sound.ARROW_HIT),
  LEVEL_UP(Sound.LEVEL_UP),
  DRINK(Sound.DRINK),
  EAT(Sound.EAT),
  BURP(Sound.BURP),
  CLICK(Sound.CLICK),
  HIT(Sound.SUCCESSFUL_HIT),

  // Notes
  SNARE(Sound.NOTE_SNARE_DRUM),
  BASS(Sound.NOTE_BASS),
  PIANO(Sound.NOTE_PIANO),
  PLING(Sound.NOTE_PLING),

  // Entities
  FIREWORK(Sound.FIREWORK_BLAST),
  LAUNCH(Sound.FIREWORK_LAUNCH),
  PLOP(Sound.ITEM_PICKUP),
  EXPLOSION(Sound.EXPLODE),
  POP(Sound.LAVA_POP),

  // Blocks
  ANVIL(Sound.ANVIL_LAND),
  DOOR_HIT(Sound.ZOMBIE_WOOD),
  DOOR_BREAK(Sound.ZOMBIE_WOODBREAK),

  // Mobs
  MEOW(Sound.CAT_MEOW),
  ENDERDRAGON(Sound.ENDERDRAGON_GROWL),
  GOLEM_DEATH(Sound.IRONGOLEM_DEATH),
  BARK(Sound.WOLF_BARK),
  ZOMBIE_MOAN(Sound.ZOMBIE_IDLE),
  BLAZE_BREATH(Sound.BLAZE_DEATH),
  BLAZE_HIT(Sound.BLAZE_HIT),
  BLAZE_DEATH(Sound.BLAZE_DEATH),
  TELEPORT(Sound.ENDERMAN_TELEPORT),
  GHAST_DEATH(Sound.GHAST_DEATH),
  MOAN(Sound.GHAST_MOAN),
  SPIDER(Sound.SPIDER_DEATH),
  HMMM(Sound.VILLAGER_HAGGLE),
  WITHER_SPAWN(Sound.WITHER_SPAWN),
  WITHER_SHOOT(Sound.WITHER_SHOOT),
  WITHER_DEATH(Sound.WITHER_DEATH),
  REMEDY(Sound.ZOMBIE_REMEDY),;

  private final Sound sound;

  SoundType(Sound sound) {
    this.sound = sound;
  }

  public String prettyName() {
    return WordUtils.capitalize(name().replaceAll("_", " ").toLowerCase());
  }

  public void play(Player player, float pitch) {
    if (this != NONE) {
      player.playSound(player.getLocation(), this.sound, 1.0F, pitch);
    }
  }
}
