package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UnlocalizedText implements Localizable {
    private final String text;
    private final List<Localizable> arguments;
    private final TextStyle style;

    public UnlocalizedText(String text, Localizable... arguments) {
        this(text, TextStyle.create(), arguments);
    }

    public UnlocalizedText(String text, TextStyle style, Localizable... arguments) {
        this(text, style, new ArrayList<>(Arrays.asList(arguments)));
    }

    public UnlocalizedText(String text, TextStyle style, List<Localizable> arguments) {
        this.text = text;
        this.arguments = arguments;
        this.style = style;
    }

    @Override
    public TextComponent translate(Locale locale) {
        String format = this.text;

        List<TextComponent> parts = new ArrayList<>();

        for (int i = 0; i < this.arguments.size(); i++) {
            Localizable curr = this.arguments.get(i);
            curr.style().inherit(this.style);

            if (format.contains("{" + i + "}")) {
                String[] split = format.split("\\{" + i + "\\}");

                parts.add(this.style.apply(split[0]));
                parts.add(curr.translate(locale));

                if (split.length > 1)
                    format = split[1];
                else
                    format = "";
            }
        }

        if (format.length() > 0)
            parts.add(this.style.apply(format));

        TextComponent result = new TextComponent(parts.get(0));
        for (int i = 1; i < parts.size(); i++)
            result.addExtra(parts.get(i));

        return result;
    }

    @Override
    public TextStyle style() {
        return this.style;
    }

    @Override
    public UnlocalizedText duplicate() {
        List<Localizable> arguments = new ArrayList<>();

        for (Localizable argument : this.arguments)
            arguments.add(argument.duplicate());

        return new UnlocalizedText(this.text, this.style, arguments);
    }
}
