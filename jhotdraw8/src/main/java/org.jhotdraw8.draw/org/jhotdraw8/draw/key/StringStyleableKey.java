/*
 * @(#)StringStyleableKey.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.NonNullMapAccessor;
import org.jhotdraw8.css.text.CssStringConverter;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.text.StyleConverterAdapter;

import java.util.function.Function;

/**
 * StringStyleableKey.
 *
 * @author Werner Randelshofer
 */
public class StringStyleableKey extends AbstractStyleableKey<@NonNull String>
        implements WriteableStyleableMapAccessor<@NonNull String>, NonNullMapAccessor<@NonNull String> {

    static final long serialVersionUID = 1L;
    private final @NonNull CssMetaData<? extends Styleable, @NonNull String> cssMetaData;

    /**
     * Creates a new instance with the specified name and with an empty String
     * as the default value.
     *
     * @param name The name of the key.
     */
    public StringStyleableKey(@NonNull String name) {
        this(name, "");
    }

    /**
     * Creates a new instance with the specified name and default value.
     *
     * @param name         The name of the key.
     * @param defaultValue The default value.
     */
    public StringStyleableKey(@NonNull String name, @NonNull String defaultValue) {
        this(name, defaultValue, null);
    }


    /**
     * Creates a new instance with the specified name, mask and default value.
     *
     * @param name         The name of the key.
     * @param defaultValue The default value.
     * @param helpText     the help text
     */
    public StringStyleableKey(String name, String defaultValue, String helpText) {
        this(null, name, defaultValue, helpText);
    }

    public StringStyleableKey(String namespace, String name, String defaultValue, String helpText) {
        super(namespace, name, String.class, false, defaultValue);
        converter = new CssStringConverter(false, '\'', helpText);
        Function<Styleable, StyleableProperty<String>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, String> converter = new StyleConverterAdapter<>(this.converter);
        CssMetaData<Styleable, String> md
                = new SimpleCssMetaData<>(property, function,
                converter, defaultValue, inherits);
        cssMetaData = md;
    }

    @Override
    public @NonNull CssMetaData<? extends @NonNull Styleable, String> getCssMetaData() {
        return cssMetaData;

    }

    private final @NonNull CssStringConverter converter;

    @Override
    public @NonNull Converter<String> getCssConverter() {
        return converter;
    }
}
