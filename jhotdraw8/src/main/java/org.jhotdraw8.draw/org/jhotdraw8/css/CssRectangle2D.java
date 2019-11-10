/*
 * @(#)CssRectangle2D.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;

import java.util.Objects;

/**
 * Represents a rectangle with x, y, width and height values specified as {@link CssSize}s.
 *
 * @author Werner Randelshofer
 */
public class CssRectangle2D {

    public final static CssRectangle2D ZERO = new CssRectangle2D();

    private final CssSize width;
    private final CssSize height;
    private final CssSize y;
    private final CssSize x;

    public CssRectangle2D(@NonNull Bounds b) {
        this(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    }

    public CssRectangle2D(@NonNull CssPoint2D a, @NonNull CssPoint2D b) {
        this(CssSize.min(a.getX(), b.getX()),
                CssSize.min(a.getY(), b.getY()),
                a.getX().subtract(b.getX()).abs(),
                a.getY().subtract(b.getY()).abs());

    }

    public CssRectangle2D(CssSize x, CssSize y, CssSize width, CssSize height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CssRectangle2D(double x, double y, double width, double height, String units) {
        this(new CssSize(x, units), new CssSize(y, units), new CssSize(width, units), new CssSize(height, units));
    }

    public CssRectangle2D() {
        this(CssSize.ZERO, CssSize.ZERO, CssSize.ZERO, CssSize.ZERO);
    }

    public CssRectangle2D(double x, double y, double width, double height) {
        this(x, y, width, height, null);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CssRectangle2D other = (CssRectangle2D) obj;
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        if (!Objects.equals(this.width, other.width)) {
            return false;
        }
        return Objects.equals(this.height, other.height);
    }

    public CssSize getWidth() {
        return width;
    }

    public CssSize getHeight() {
        return height;
    }

    public CssSize getMinY() {
        return y;
    }

    public CssSize getMinX() {
        return x;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.x);
        hash = 89 * hash + Objects.hashCode(this.y);
        hash = 89 * hash + Objects.hashCode(this.width);
        hash = 89 * hash + Objects.hashCode(this.height);
        return hash;
    }

    @NonNull
    @Override
    public String toString() {
        return "CssRectangle2D{" +
                "" + x +
                ", " + y +
                ", " + width +
                ", " + height +
                '}';
    }

    @NonNull
    public Rectangle2D getConvertedValue() {
        return new Rectangle2D(x.getConvertedValue(), y.getConvertedValue(), width.getConvertedValue(), height.getConvertedValue());

    }

    @NonNull
    public Bounds getConvertedBoundsValue() {
        return new BoundingBox(x.getConvertedValue(), y.getConvertedValue(), width.getConvertedValue(), height.getConvertedValue());

    }

    @NonNull
    public CssSize getMaxX() {
        return x.add(getWidth());
    }

    @NonNull
    public CssSize getMaxY() {
        return y.add(getHeight());
    }

    @NonNull
    public CssPoint2D getTopLeft() {
        return new CssPoint2D(x, y);
    }
}
