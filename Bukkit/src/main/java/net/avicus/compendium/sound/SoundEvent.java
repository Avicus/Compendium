package net.avicus.compendium.sound;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SoundEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter final Player player;
    @Getter private final SoundLocation location;
    @Getter @Setter private SoundType sound;

    public SoundEvent(Player player, SoundType sound, SoundLocation location) {
        this.player = player;
        this.sound = sound;
        this.location = location;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
