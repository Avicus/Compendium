package net.avicus.compendium.sound;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SoundEvent extends Event {
    @Getter final Player player;
    @Getter @Setter private SoundType sound;
    @Getter private final SoundLocation location;

    public SoundEvent(Player player, SoundType sound, SoundLocation location) {
        this.player = player;
        this.sound = sound;
        this.location = location;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
