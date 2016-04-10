package net.avicus.compendium.locale.text;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class TextStyle<T extends TextStyle> {
    private Optional<ChatColor> color = Optional.empty();
    private Optional<Boolean> bold = Optional.empty();
    private Optional<Boolean> italic = Optional.empty();
    private Optional<Boolean> underlined = Optional.empty();
    private Optional<Boolean> magic = Optional.empty();
    private Optional<Boolean> strike = Optional.empty();
    private Optional<ClickEvent> clickEvent = Optional.empty();
    private Optional<HoverEvent> hoverEvent = Optional.empty();

    /**
     * Takes styles from the provided TextStyle writes them
     * into this TextStyle if they aren't present.
     * @param style
     */
    protected void inherit(TextStyle style) {
        this.color = this.color.isPresent() ? this.color : style.color;
        this.bold = this.bold.isPresent() ? this.bold : style.bold;
        this.italic = this.italic.isPresent() ? this.italic : style.italic;
        this.underlined = this.underlined.isPresent() ? this.underlined : style.underlined;
        this.magic = this.magic.isPresent() ? this.magic : style.magic;
        this.strike = this.strike.isPresent() ? this.strike : style.strike;
        this.clickEvent = this.clickEvent.isPresent() ? this.clickEvent : style.clickEvent;
        this.hoverEvent = this.hoverEvent.isPresent() ? this.hoverEvent : style.hoverEvent;
    }

    public T color(ChatColor color) {
        this.color = Optional.ofNullable(color);
        return (T) this;
    }

    public T bold(boolean bold) {
        this.bold = Optional.ofNullable(bold);
        return (T) this;
    }

    public T italic(boolean italic) {
        this.italic = Optional.ofNullable(italic);
        return (T) this;
    }

    public T underlined(boolean underlined) {
        this.underlined = Optional.ofNullable(underlined);
        return (T) this;
    }

    public T magic(boolean magic) {
        this.magic = Optional.ofNullable(magic);
        return (T) this;
    }

    public T strike(boolean strike) {
        this.strike = Optional.ofNullable(strike);
        return (T) this;
    }

    public T event(ClickEvent event) {
        this.clickEvent = Optional.ofNullable(event);
        return (T) this;
    }

    public T event(HoverEvent event) {
        this.hoverEvent = Optional.ofNullable(event);
        return (T) this;
    }

    protected TextComponent toComponent(String text) {
        TextComponent message = new TextComponent(text);
        if (this.color.isPresent())
            message.setColor(net.md_5.bungee.api.ChatColor.valueOf(this.color.get().name()));
        if (this.bold.isPresent())
            message.setBold(this.bold.get());
        if (this.italic.isPresent())
            message.setItalic(this.italic.get());
        if (this.underlined.isPresent())
            message.setUnderlined(this.underlined.get());
        if (this.magic.isPresent())
            message.setObfuscated(this.magic.get());
        if (this.strike.isPresent())
            message.setStrikethrough(this.strike.get());
        if (this.clickEvent.isPresent())
            message.setClickEvent(this.clickEvent.get());
        if (this.hoverEvent.isPresent())
            message.setHoverEvent(this.hoverEvent.get());
        return message;
    }

    protected String toLegacyText(String text) {
        return toComponent(text).toLegacyText();
    }
}
