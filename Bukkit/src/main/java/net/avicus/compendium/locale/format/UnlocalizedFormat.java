package net.avicus.compendium.locale.format;

import net.avicus.compendium.locale.text.LocalizableText;
import net.avicus.compendium.locale.text.UnlocalizedText;

public class UnlocalizedFormat implements LocalizableFormat {
    private final String format;

    public UnlocalizedFormat(String format) {
        this.format = format;
    }

    @Override
    public UnlocalizedText text(LocalizableText... arguments) {
        return new UnlocalizedText(this.format, arguments);
    }
}
