package net.avicus.compendium.sound;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum SoundType {

    NONE(null),
    DING(Sound.ARROW_HIT),
    LEVEL_UP(Sound.LEVEL_UP),
    BURP(Sound.BURP),
    BARK(Sound.WOLF_BARK),
    LAUNCH(Sound.FIREWORK_LAUNCH),
    SNARE(Sound.NOTE_SNARE_DRUM),
    ANVIL(Sound.ANVIL_LAND),
    FIREWORK(Sound.FIREWORK_BLAST),
    BASS(Sound.NOTE_BASS),
    PIANO(Sound.NOTE_PIANO),
    PLING(Sound.NOTE_PLING),
    PLOP(Sound.ITEM_PICKUP),
    ENDERDRAGON(Sound.ENDERDRAGON_GROWL);
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
