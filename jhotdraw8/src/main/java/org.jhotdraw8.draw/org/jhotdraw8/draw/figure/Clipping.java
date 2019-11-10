/*
 * @(#)Clipping.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;


import org.jhotdraw8.annotation.NonNull;

/**
 * Defines a <i>clipping</i> of a {@link Drawing}.
 * <p>
 * A clipping is used to hold a selection of figures, so that they can be read
 * or written to the clipboard.
 * <p>
 * A clipping can not have a parent, and thus returns false in
 * isSuitableParent(parent) for all parents except null.
 *
 * @author Werner Randelshofer
 */
public interface Clipping extends Figure {

    /**
     * The CSS type selector for a label object is {@value #TYPE_SELECTOR}.
     */
    String TYPE_SELECTOR = "Clipping";

    /**
     * Clipping figures always return false for isSelectable.
     */
    @Override
    default boolean isSelectable() {
        return false;
    }

    @NonNull
    @Override
    default String getTypeSelector() {
        return TYPE_SELECTOR;
    }
}
