/*
 * @(#)ObservableWordListStyleableKey.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.reflect.TypeToken;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.text.StyleConverterAdapter;
import org.jhotdraw8.xml.text.XmlWordListConverter;

import java.util.function.Function;

/**
 * ObservableWordListStyleableKey.
 *
 * @author Werner Randelshofer
 */
public class ObservableWordListStyleableKey extends AbstractStyleableKey<ImmutableList<String>> implements WriteableStyleableMapAccessor<ImmutableList<String>> {

    private static final long serialVersionUID = 1L;

    private final @NonNull CssMetaData<?, ImmutableList<String>> cssMetaData;

    /**
     * Creates a new instance with the specified name and with null as the
     * default value.
     *
     * @param name The name of the key.
     */
    public ObservableWordListStyleableKey(@NonNull String name) {
        this(name, null);
    }

    /**
     * Creates a new instance with the specified name, mask and default value.
     *  @param name         The name of the key.
     * @param defaultValue The default value.
     */
    public ObservableWordListStyleableKey(@NonNull String name, ImmutableList<String> defaultValue) {
        super(name, new TypeToken<ImmutableList<String>>() {
        }, defaultValue);

        Function<Styleable, StyleableProperty<ImmutableList<String>>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, ImmutableList<String>> converter
                = new StyleConverterAdapter<>(new XmlWordListConverter());
        CssMetaData<Styleable, ImmutableList<String>> md
                = new SimpleCssMetaData<>(property, function,
                converter, defaultValue, inherits);
        cssMetaData = md;
    }

    @Override
    public @NonNull CssMetaData<? extends @NonNull Styleable, ImmutableList<String>> getCssMetaData() {
        return cssMetaData;

    }

    private Converter<ImmutableList<String>> converter;

    @Override
    public @NonNull Converter<ImmutableList<String>> getCssConverter() {
        if (converter == null) {
            converter = new XmlWordListConverter();
        }
        return converter;
    }
}
