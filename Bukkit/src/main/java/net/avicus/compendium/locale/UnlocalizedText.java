package net.avicus.compendium.locale;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UnlocalizedText extends LocalizableText<UnlocalizedText> {
    private final String format;
    private final List<LocalizableText> arguments;
    private final List<LocalizableText> extras;

    public UnlocalizedText(String format, LocalizableText... arguments) {
        this(format, new ArrayList<>(Arrays.asList(arguments)));
    }

    public UnlocalizedText(String format, List<LocalizableText> arguments) {
        this(format, arguments, new ArrayList<>());
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
            LocalizableText curr = this.arguments.get(i).duplicate();

            if (format.contains("{" + i + "}")) {
                String[] split = format.split("\\{" + i + "\\}");

                curr.inherit(this);

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
            LocalizableText text = extra.duplicate();
            text.inherit(this);
            result.addExtra(text.toComponent(locale));
        }

        return result;
    }
    @Override
    public UnlocalizedText duplicate() {
        List<LocalizableText> arguments = new ArrayList<>();
        List<LocalizableText> extras = new ArrayList<>();
        for (LocalizableText arg : this.arguments)
            arguments.add(arg.duplicate());
        for (LocalizableText extra : this.extras)
            extras.add(extra.duplicate());

        UnlocalizedText text = new UnlocalizedText(this.format, arguments, extras);
        text.inherit(this);
        return text;
    }
}
