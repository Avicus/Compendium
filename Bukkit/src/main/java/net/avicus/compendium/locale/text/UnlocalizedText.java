package net.avicus.compendium.locale.text;

import net.avicus.compendium.locale.LocalizableText;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UnlocalizedText extends TextStyle<UnlocalizedText> implements LocalizableText {
    private final String format;
    private final List<LocalizableText> arguments;
    private final List<LocalizableText> extras;

    public UnlocalizedText(String format, LocalizableText... arguments) {
        this(format, Arrays.asList(arguments), new ArrayList<>());
    }

    public UnlocalizedText(String format, List<LocalizableText> arguments, List<LocalizableText> extras) {
        this.format = format;
        this.arguments = arguments;
        this.extras = extras;
    }

    public void addExtra(LocalizableText extra) {
        this.extras.add(extra);
    }

    public TextComponent toComponent(Locale locale) {
        String format = this.format;

        List<TextComponent> parts = new ArrayList<>();

        // Replace {0}, {1}, etc.
        for (int i = 0; i < this.arguments.size(); i++) {
            LocalizableText curr = this.arguments.get(i);

            if (format.contains("{" + i + "}")) {
                String[] split = format.split("\\{" + i + "\\}");

                TextStyle currStyle = new TextStyle();
                if (curr instanceof TextStyle)
                    currStyle = (TextStyle) curr;
                currStyle.inherit(this);

                parts.add(toComponent(split[0]));
                parts.add(curr.toComponent(locale));

                if (split.length > 1)
                    format = split[1];
                else
                    format = "";
            }
        }

        // last bit (punctuation, typically)
        if (format.length() > 0)
            parts.add(toComponent(format));

        if (parts.size() == 0)
            return new TextComponent("");

        // Base text
        TextComponent result = new TextComponent(parts.get(0));
        for (int i = 1; i < parts.size(); i++)
            result.addExtra(parts.get(i));

        // Extras
        for (LocalizableText extra : this.extras) {
            TextStyle style = this;
            if (extra instanceof TextStyle)
                style = (TextStyle) extra;
            style.inherit(this);

            result.addExtra(extra.toComponent(locale));
        }

        return result;
    }
}
