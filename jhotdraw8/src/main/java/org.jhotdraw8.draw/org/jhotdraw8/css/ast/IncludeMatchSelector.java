/* @(#)IncludeMatchSelector.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.ast;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.SelectorModel;

import java.util.function.Consumer;

/**
 * An "include match selector" {@code ~=} matches an element if the element has
 * an attribute with the specified name and the attribute value contains a word
 * list with the specified word.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class IncludeMatchSelector extends AbstractAttributeSelector {
    @Nullable
    private final String namespace;
    @Nonnull
    private final String attributeName;
    @Nonnull
    private final String word;

    public IncludeMatchSelector(@Nullable String namespace, @Nonnull String attributeName, @Nonnull String word) {
        this.namespace = namespace;
        this.attributeName = attributeName;
        this.word = word;
    }

    @Nullable
    @Override
    protected <T> T match(@Nonnull SelectorModel<T> model, T element) {
        return model.attributeValueContainsWord(element, namespace, attributeName, word) ? element : null;
    }

    @Nonnull
    @Override
    public String toString() {
        return "[" + attributeName + "~=" + word + ']';
    }

    @Override
    public void produceTokens(Consumer<CssToken> consumer) {
        consumer.accept(new CssToken(CssTokenType.TT_LEFT_SQUARE_BRACKET));
        if (namespace != null) {
            consumer.accept(new CssToken(CssTokenType.TT_IDENT, namespace));
            consumer.accept(new CssToken(CssTokenType.TT_VERTICAL_LINE));
        }
        consumer.accept(new CssToken(CssTokenType.TT_INCLUDE_MATCH));
        consumer.accept(new CssToken(CssTokenType.TT_STRING, word));
        consumer.accept(new CssToken(CssTokenType.TT_RIGHT_SQUARE_BRACKET));
    }
}
