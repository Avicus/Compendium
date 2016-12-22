package net.avicus.compendium.text;

import net.md_5.bungee.api.chat.BaseComponent;

public final class Components {

    private Components() {
    }

    public static BaseComponent copyStyle(BaseComponent source, BaseComponent destination) {
        destination.setColor(source.getColorRaw());
        destination.setBold(source.isBoldRaw());
        destination.setItalic(source.isItalicRaw());
        destination.setUnderlined(source.isUnderlinedRaw());
        destination.setStrikethrough(source.isStrikethroughRaw());
        destination.setObfuscated(source.isObfuscatedRaw());
        destination.setInsertion(source.getInsertion());
        destination.setClickEvent(source.getClickEvent());
        destination.setHoverEvent(source.getHoverEvent());

        return destination;
    }

}
