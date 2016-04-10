package net.avicus.compendium.locale.format;

import net.avicus.compendium.locale.text.LocalizableText;

public interface LocalizableFormat {
    LocalizableText text(LocalizableText... arguments);
}
