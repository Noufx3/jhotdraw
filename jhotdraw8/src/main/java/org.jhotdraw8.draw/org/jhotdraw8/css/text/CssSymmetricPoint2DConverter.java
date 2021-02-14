/*
 * @(#)CssSymmetricPoint2DConverter.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.text;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.io.IdResolver;
import org.jhotdraw8.io.IdSupplier;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.text.PatternConverter;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.ParseException;

/**
 * Converts a {@code javafx.geometry.Point2D} into a {@code String} and vice
 * versa. If the X and the Y-value are identical, then only one value is output.
 *
 * @author Werner Randelshofer
 */
public class CssSymmetricPoint2DConverter implements Converter<CssPoint2D> {

    private final PatternConverter formatter = new PatternConverter("{0,list,{1,size}|[ ]+}", new CssConverterFactory());

    @Override
    public void toString(Appendable out, @Nullable IdSupplier idSupplier, @NonNull CssPoint2D value) throws IOException {
        CssSize x = value.getX();
        CssSize y = value.getY();
        if (x == y) {
            formatter.toStr(out, idSupplier, 1, value.getX());
        } else {
            formatter.toStr(out, idSupplier, 2, value.getX(), value.getY());
        }
    }

    @Override
    public @NonNull CssPoint2D fromString(@NonNull CharBuffer buf, @Nullable IdResolver idResolver) throws ParseException, IOException {
        Object[] v = formatter.fromString(buf);
        int count = (Integer) v[0];
        switch (count) {
        case 1:
            return new CssPoint2D(((CssSize) v[1]), ((CssSize) v[1]));
        case 2:
            return new CssPoint2D(((CssSize) v[1]), ((CssSize) v[2]));
        default:
            throw new ParseException("one or two numbers expected, found " + count + " numbers", 0);
        }
    }

    @Override
    public @Nullable CssPoint2D getDefaultValue() {
        return new CssPoint2D(CssSize.ZERO, CssSize.ZERO);
    }

    @Override
    public @NonNull String getHelpText() {
        return "Format of ⟨SymmetricSize2D⟩: ⟨xy⟩ ｜ ⟨x⟩ ⟨y⟩";
    }

}
