package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.avicus.magnet.api.text.translation.TranslatableComponent;

/**
 * Represents anything that can be translated and sent to players.
 */
public interface Localizable extends TranslatableComponent {

    Localizable[] EMPTY = new Localizable[0];

    /**
     * Get the style of this.
     *
     * @return
     */
    TextStyle style();

    /**
     * Copy this and its styles.
     *
     * @return
     */
    Localizable duplicate();
}
