/* @(#)CssSizeInsets.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */

package org.jhotdraw8.text;

import java.util.Objects;
import javafx.geometry.Insets;

/**
 * CssSizeInsets.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class CssSizeInsets {
private final CssSize top;
private final CssSize right;
private final CssSize bottom;
private final CssSize left;

    public CssSizeInsets(CssSize top, CssSize right, CssSize bottom, CssSize left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    CssSizeInsets() {
       this(CssSize.ZERO,CssSize.ZERO, CssSize.ZERO,CssSize.ZERO);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CssSizeInsets other = (CssSizeInsets) obj;
        if (!Objects.equals(this.top, other.top)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        if (!Objects.equals(this.bottom, other.bottom)) {
            return false;
        }
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        return true;
    }

    public CssSize getBottom() {
        return bottom;
    }

    public CssSize getLeft() {
        return left;
    }

    public CssSize getRight() {
        return right;
    }

    public CssSize getTop() {
        return top;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.top);
        hash = 89 * hash + Objects.hashCode(this.right);
        hash = 89 * hash + Objects.hashCode(this.bottom);
        hash = 89 * hash + Objects.hashCode(this.left);
        return hash;
    }

    
    public Insets getDefaultConvertedValue() {
        return new Insets(top.getDefaultConvertedValue(),right.getDefaultConvertedValue(),
        bottom.getDefaultConvertedValue(),left.getDefaultConvertedValue());
    }
}
