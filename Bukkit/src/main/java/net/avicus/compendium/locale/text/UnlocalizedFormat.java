package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;

public class UnlocalizedFormat implements LocalizableFormat<UnlocalizedText> {
    private final String text;

    public UnlocalizedFormat(String text) {
        this.text = text;
    }

    @Override
    public UnlocalizedText with(TextStyle style, Localizable... arguments) {
        return new UnlocalizedText(this.text, style, arguments);
    }

    @Override
    public UnlocalizedText with(Localizable... arguments) {
        return new UnlocalizedText(this.text, arguments);
    }
}
