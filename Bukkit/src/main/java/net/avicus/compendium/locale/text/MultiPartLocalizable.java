package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Locale;

public class MultiPartLocalizable implements Localizable {

    private final Localizable[] args;

    public MultiPartLocalizable(Localizable... args) {
        this.args = args;
    }

    @Override
    public TextStyle style() {
        return null;
    }

    @Override
    public Localizable duplicate() {
        return null;
    }

    @Override
    public BaseComponent translate(Locale locale) {
        TextComponent message = new TextComponent(this.args[0].translate(locale));

        for (int i = 1; i < this.args.length; i++) {
            message.addExtra(this.args[i].translate(locale));
        }

        return message;
    }
}
