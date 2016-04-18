package net.avicus.compendium;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class TextStyle {
    private Optional<ChatColor> color = Optional.empty();
    private Optional<Boolean> bold = Optional.empty();
    private Optional<Boolean> italic = Optional.empty();
    private Optional<Boolean> underlined = Optional.empty();
    private Optional<Boolean> magic = Optional.empty();
    private Optional<Boolean> strike = Optional.empty();
    private Optional<ClickEvent> click = Optional.empty();
    private Optional<HoverEvent> hover = Optional.empty();

    public static TextStyle ofBold() {
        return new TextStyle().bold();
    }

    public static TextStyle ofColor(ChatColor color) {
        return new TextStyle().color(color);
    }

    public static TextStyle create() {
        return new TextStyle();
    }

    private TextStyle() {

    }

    public TextStyle duplicate() {
        return new TextStyle().inherit(this);
    }

    public TextComponent apply(String text) {
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
        if (this.click.isPresent())
            message.setClickEvent(this.click.get());
        if (this.hover.isPresent())
            message.setHoverEvent(this.hover.get());
        return message;
    }

    public TextStyle inherit(TextStyle parent) {
        this.color = this.color.isPresent() ? this.color : parent.color;
        this.bold = this.bold.isPresent() ? this.bold : parent.bold;
        this.italic = this.italic.isPresent() ? this.italic : parent.italic;
        this.underlined = this.underlined.isPresent() ? this.underlined : parent.underlined;
        this.magic = this.magic.isPresent() ? this.magic : parent.magic;
        this.strike = this.strike.isPresent() ? this.strike : parent.strike;
        this.click = this.click.isPresent() ? this.click : parent.click;
        this.hover = this.hover.isPresent() ? this.hover : parent.hover;
        return this;
    }

    public TextStyle reset() {
        this.color = Optional.empty();
        this.bold = Optional.empty();
        this.italic = Optional.empty();
        this.underlined = Optional.empty();
        this.magic = Optional.empty();
        this.strike = Optional.empty();
        this.click = Optional.empty();
        this.hover = Optional.empty();
        return this;
    }

    public TextStyle color(ChatColor color) {
        this.color = Optional.ofNullable(color);
        return this;
    }

    public TextStyle bold(boolean bold) {
        this.bold = Optional.ofNullable(bold);
        return this;
    }

    public TextStyle bold() {
        return bold(true);
    }

    public TextStyle italic(boolean italic) {
        this.italic = Optional.ofNullable(italic);
        return this;
    }

    public TextStyle italic() {
        return italic(true);
    }

    public TextStyle underlined(boolean underlined) {
        this.underlined = Optional.ofNullable(underlined);
        return this;
    }

    public TextStyle underlined() {
        return underlined(true);
    }

    public TextStyle magic(boolean magic) {
        this.magic = Optional.ofNullable(magic);
        return this;
    }

    public TextStyle magic() {
        return magic(true);
    }

    public TextStyle strike(boolean strike) {
        this.strike = Optional.ofNullable(strike);
        return this;
    }

    public TextStyle strike() {
        return strike(true);
    }

    public TextStyle click(ClickEvent event) {
        this.click = Optional.ofNullable(event);
        return this;
    }

    public TextStyle hover(HoverEvent event) {
        this.hover = Optional.ofNullable(event);
        return this;
    }
}
