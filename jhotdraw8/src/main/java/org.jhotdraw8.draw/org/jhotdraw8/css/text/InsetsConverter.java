/*
 * @(#)InsetsConverter.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.text;

import javafx.geometry.Insets;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.io.IdResolver;
import org.jhotdraw8.io.IdSupplier;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Converts a {@link Insets} object into a {@code String} and vice
 * versa.
 * <p>
 * List of four sizes in the sequence top, right, bottom, left. If left is
 * omitted, it is the same as right. If bottom is omitted, it is the same as
 * top. If right is omitted it is the same as top.
 * <pre>
 * insets       = top-right-bottom-left ;
 * insets       = top-bottom, right-left ;
 * insets       = top , right , bottom, left ;
 * </pre> *
 *
 * @author Werner Randelshofer
 */
public class InsetsConverter extends AbstractCssConverter<Insets> {
    public InsetsConverter(boolean nullable) {
        super(nullable);
    }

    @Override
    public @NonNull Insets parseNonNull(@NonNull CssTokenizer tt, @Nullable IdResolver idResolver) throws ParseException, IOException {
        List<Number> list = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            switch (tt.next()) {
            case CssTokenType.TT_NUMBER:
                list.add(tt.currentNumberNonNull());
                break;
            case CssTokenType.TT_COMMA:
                break;
            default:
                tt.pushBack();
                    break;
            }
        }
        switch (list.size()) {
            case 1:
                double trbl = list.get(0).doubleValue();
                return new Insets(trbl);
            case 2:
                double tb = list.get(0).doubleValue();
                double rl = list.get(1).doubleValue();
                return new Insets(tb, rl, tb, rl);
            case 4:
                double t = list.get(0).doubleValue();
                double r = list.get(1).doubleValue();
                double b = list.get(2).doubleValue();
                double l = list.get(3).doubleValue();
                return new Insets(t, r, b, l);
            default:
                throw new ParseException("⟨Insets⟩: ⟨top-right-bottom-left⟩ ｜ ⟨top-bottom⟩,⟨left-right⟩ ｜ ⟨top⟩,⟨right⟩,⟨bottom⟩,⟨left⟩ expected.", tt.getStartPosition());

        }
    }

    @Override
    public @NonNull String getHelpText() {
        return "Format of ⟨Insets⟩: ⟨top-right-bottom-left⟩ ｜ ⟨top-bottom⟩ ⟨left-right⟩ ｜ ⟨top⟩ ⟨right⟩ ⟨bottom⟩ ⟨left⟩";
    }

    @Override
    protected <TT extends Insets> void produceTokensNonNull(@NonNull TT value, @Nullable IdSupplier idSupplier, @NonNull Consumer<CssToken> out) {
        if (value.getRight() == value.getLeft()) {
            if (value.getTop() == value.getBottom()) {
                if (value.getTop() == value.getLeft()) {
                    out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getTop()));
                    return;
                } else {
                    out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getTop()));
                    out.accept(new CssToken(CssTokenType.TT_S, " "));
                    out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getRight()));
                    return;
                }
            }
        }
        out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getTop()));
        out.accept(new CssToken(CssTokenType.TT_S, " "));
        out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getRight()));
        out.accept(new CssToken(CssTokenType.TT_S, " "));
        out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getBottom()));
        out.accept(new CssToken(CssTokenType.TT_S, " "));
        out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getLeft()));
    }
}
