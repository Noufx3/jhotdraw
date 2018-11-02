/* @(#)Dimension2DStyleableFigureKey.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import java.util.function.Function;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javax.annotation.Nonnull;

import org.jhotdraw8.css.text.Dimension2D;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.css.text.CssSize2DConverter;
import org.jhotdraw8.text.StyleConverterAdapter;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;

/**
 * Dimension2DStyleableFigureKey.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class Dimension2DStyleableFigureKey extends AbstractStyleableFigureKey<Dimension2D> implements WriteableStyleableMapAccessor<Dimension2D> {

    private final static long serialVersionUID = 1L;
    private Converter<Dimension2D> converter;

    @Nonnull
    private final CssMetaData<?, Dimension2D> cssMetaData;

    /**
     * Creates a new instance with the specified name and with null as the
     * default value.
     *
     * @param name The name of the key.
     */
    public Dimension2DStyleableFigureKey(String name) {
        this(name, null);
    }

    /**
     * Creates a new instance with the specified name and default value.
     *
     * @param name The name of the key.
     * @param defaultValue The default value.
     */
    public Dimension2DStyleableFigureKey(String name, Dimension2D defaultValue) {
        this(name, DirtyMask.of(DirtyBits.NODE), defaultValue);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value, and allowing or disallowing null values.
     *
     * @param key The name of the name. type parameters are given. Otherwise
     * specify them in arrow brackets.
     * @param mask Dirty bit mask.
     * @param defaultValue The default value.
     */
    public Dimension2DStyleableFigureKey(String key, DirtyMask mask, Dimension2D defaultValue) {
        super(key, Dimension2D.class, mask, defaultValue);

        Function<Styleable, StyleableProperty<Dimension2D>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, Dimension2D> converter
                = new StyleConverterAdapter<>(new CssSize2DConverter());
        CssMetaData<Styleable, Dimension2D> md
                = new SimpleCssMetaData<>(property, function,
                converter, defaultValue, inherits);
        cssMetaData = md;
    }


    @Override
    public Converter<Dimension2D> getConverter() {
        if (converter == null) {
            converter = new CssSize2DConverter();
        }
        return converter;
    }
    @Nonnull
    @Override
    public CssMetaData<?, Dimension2D> getCssMetaData() {
      return cssMetaData;
      
    }
}