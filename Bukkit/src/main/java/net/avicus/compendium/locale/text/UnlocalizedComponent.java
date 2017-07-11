package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;
import net.md_5.bungee.api.chat.BaseComponent;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * A {@link BaseComponent} that can be style like a {@link Localizable}.
 */
public class UnlocalizedComponent implements Localizable {

    private final BaseComponent component;
    @Nullable private TextStyle style;

    public UnlocalizedComponent(BaseComponent component) {
        this(component, null);
    }

    public UnlocalizedComponent(BaseComponent component, @Nullable TextStyle style) {
        this.component = component;
        this.style = style;
    }

    @Override
    public TextStyle style() {
        if (this.style == null) {
            this.style = TextStyle.from(this.component);
        }

        return this.style;
    }

    @Override
    public Localizable duplicate() {
        return new UnlocalizedComponent(this.component, this.style != null ? this.style.duplicate() : null);
    }

    @Override
    public BaseComponent translate(Locale locale) {
        if (this.style != null) {
            this.style.apply(this.component);
        }

        return this.component;
    }
}
