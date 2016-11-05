package net.avicus.compendium.sound;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum SoundType {

    NONE(null),
    DING(Sound.ENTITY_ARROW_HIT),
    LEVEL_UP(Sound.ENTITY_PLAYER_LEVELUP),
    BURP(Sound.ENTITY_PLAYER_BURP),
    BARK(Sound.ENTITY_WOLF_GROWL),
    LAUNCH(Sound.ENTITY_FIREWORK_LAUNCH),
    SNARE(Sound.BLOCK_NOTE_SNARE),
    ANVIL(Sound.BLOCK_ANVIL_LAND),
    FIREWORK(Sound.ENTITY_FIREWORK_BLAST),
    BASS(Sound.BLOCK_NOTE_BASEDRUM),
    PIANO(Sound.BLOCK_NOTE_HARP),
    PLING(Sound.BLOCK_NOTE_PLING),
    PLOP(Sound.ENTITY_ITEM_PICKUP),
    ENDERDRAGON(Sound.ENTITY_ENDERDRAGON_GROWL),
    GOLEM_DEATH(Sound.ENTITY_IRONGOLEM_DEATH);
    private final Sound sound;

    SoundType(Sound sound) {
        this.sound = sound;
    }

    public String prettyName() {
        return StringUtils.capitalize(name().toLowerCase());
    }

    public void play(Player player, float pitch) {
        if (this != NONE)
            player.playSound(player.getLocation(), this.sound, 1.0F, pitch);
    }
}
