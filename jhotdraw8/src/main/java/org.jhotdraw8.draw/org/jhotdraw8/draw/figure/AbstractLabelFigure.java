/*
 * @(#)AbstractLabelFigure.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssRectangle2D;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.draw.connector.Connector;
import org.jhotdraw8.draw.connector.RectangleConnector;
import org.jhotdraw8.draw.key.CssPoint2DStyleableMapAccessor;
import org.jhotdraw8.draw.key.CssSizeStyleableKey;
import org.jhotdraw8.draw.locator.BoundsLocator;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.draw.render.SimpleRenderContext;
import org.jhotdraw8.geom.Shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A Label that can be placed anywhere on a drawing.
 *
 * @author Werner Randelshofer
 */
public abstract class AbstractLabelFigure extends AbstractLeafFigure
        implements TextFillableFigure, FillableFigure, StrokableFigure,
        TextFontableFigure, TextLayoutableFigure, ConnectableFigure, PathIterableFigure, ShapeableFigure,
        PaddableFigure {

    public static final @NonNull CssSizeStyleableKey ORIGIN_X = new CssSizeStyleableKey("originX", CssSize.ZERO);
    public static final @NonNull CssSizeStyleableKey ORIGIN_Y = new CssSizeStyleableKey("originY", CssSize.ZERO);
    public static final @NonNull CssPoint2DStyleableMapAccessor ORIGIN = new CssPoint2DStyleableMapAccessor("origin", ORIGIN_X, ORIGIN_Y);
    private @Nullable Bounds cachedLayoutBounds;

    public AbstractLabelFigure() {
        this(0, 0);
    }

    public AbstractLabelFigure(@NonNull Point2D position) {
        this(position.getX(), position.getY());
    }

    public AbstractLabelFigure(double x, double y) {
        // Performance: Only set properties if they differ from the default value.
        if (x != 0) {
            set(ORIGIN_X, new CssSize(x));
        }
        if (y != 0) {
            set(ORIGIN_Y, new CssSize(y));
        }
    }

    @Override
    public @NonNull Node createNode(RenderContext drawingView) {
        Group g = new Group();
        g.setManaged(false);
        g.setAutoSizeChildren(false);
        Path p = new Path();
        p.setManaged(false);
        Text text = new Text();
        text.setManaged(false);
        g.getProperties().put("pathNode", p);
        g.getProperties().put("textNode", text);
        return g;
    }

    @Override
    public @Nullable Connector findConnector(@NonNull Point2D p, Figure prototype) {
        return new RectangleConnector(new BoundsLocator(getLayoutBounds(), p));
    }

    protected @Nullable Bounds getCachedLayoutBounds() {
        return cachedLayoutBounds;
    }

    protected @Nullable Bounds setCachedLayoutBounds(Bounds newValue) {
        Bounds oldValue = cachedLayoutBounds;
        cachedLayoutBounds = newValue;
        return oldValue;
    }

    @Override
    public @NonNull Bounds getLayoutBounds() {
        Bounds boundsInLocal = getCachedLayoutBounds();
        return boundsInLocal == null ? computeLayoutBounds() : boundsInLocal;
    }

    @Override
    public @NonNull CssRectangle2D getCssLayoutBounds() {
        return new CssRectangle2D(getLayoutBounds());
    }

    /**
     * Computes the bounds of the node for layout calculations. These bounds
     * include the text of the node and the padding.
     *
     * @return the layout bounds
     */
    public @NonNull Bounds computeLayoutBounds() {
        return computeLayoutBounds(new SimpleRenderContext(), new Text());
    }

    protected @NonNull Bounds computeLayoutBounds(RenderContext ctx, Text textNode) {
        updateTextNode(ctx, textNode);
        Bounds b = textNode.getLayoutBounds();
        Insets i = getStyledNonNull(PADDING).getConvertedValue();

        return new BoundingBox(
                b.getMinX() - i.getLeft(),
                b.getMinY() - i.getTop(),
                b.getWidth() + i.getLeft() + i.getRight(),
                textNode.getBaselineOffset() + i.getTop() + i.getBottom());
    }

    /**
     * Returns the bounds of the text node for layout calculations. These bounds
     * only includes the text - without padding.
     *
     * @param ctx the render context
     * @return the layout bounds of the text
     */
    protected @NonNull Bounds getTextBounds(@Nullable RenderContext ctx) {
        Text textNode = new Text();
        updateTextNode(ctx, textNode);
        Bounds b = textNode.getLayoutBounds();
        return b;
    }

    @Override
    public PathIterator getPathIterator(RenderContext ctx, AffineTransform tx) {
        Text tn = new Text();
        tn.setX(getStyledNonNull(ORIGIN_X).getConvertedValue());
        tn.setY(getStyledNonNull(ORIGIN_Y).getConvertedValue());
        tn.setBoundsType(TextBoundsType.VISUAL);
        applyTextFontableFigureProperties(null, tn);
        applyTextLayoutableFigureProperties(null, tn);

        // We must set the font before we set the text, so that JavaFx does not need to retrieve
        // the system default font, which on Windows requires that the JavaFx Toolkit is launched.
        tn.setText(getText(null));

        return Shapes.awtShapeFromFX(tn).getPathIterator(tx);
    }

    protected abstract @Nullable String getText(RenderContext ctx);


    @Override
    public void layout(@NonNull RenderContext ctx) {
        Bounds b = computeLayoutBounds(ctx, new Text());
        setCachedLayoutBounds(b);
    }

    @Override
    public void reshapeInLocal(@NonNull CssSize x, @NonNull CssSize y, @NonNull CssSize width, @NonNull CssSize height) {
        Bounds lb = computeLayoutBounds();
        Insets i = getStyledNonNull(PADDING).getConvertedValue();
        set(ORIGIN, new CssPoint2D(x.getConvertedValue() + i.getLeft(), y.getConvertedValue() + lb.getHeight() - i.getBottom()));
    }

    @Override
    public void translateInLocal(@NonNull CssPoint2D delta) {
        set(ORIGIN, getNonNull(ORIGIN).add(delta));
    }

    protected void updateGroupNode(RenderContext ctx, Group node) {

    }

    @Override
    public void updateNode(@NonNull RenderContext ctx, @NonNull Node node) {
        Group g = (Group) node;
        Path p = (Path) g.getProperties().get("pathNode");
        Text t = (Text) g.getProperties().get("textNode");

        updateGroupNode(ctx, g);
        updatePathNode(ctx, p);
        updateTextNode(ctx, t);

        // Note: we must not add individual elements to g.children because
        // its ObservableList fires too many events.
        ArrayList<Node> newChildren = new ArrayList<>(2);
        if (p.getStroke() != null || p.getFill() != null) {
            newChildren.add(p);
        }
        if (t.getStroke() != null || t.getFill() != null) {
            newChildren.add(t);
        }
        if (!newChildren.equals(g.getChildren())) {
            g.getChildren().setAll(newChildren);
        }
    }

    protected void updatePathNode(RenderContext ctx, @NonNull Path node) {
        applyFillableFigureProperties(ctx, node);
        applyStrokableFigureProperties(ctx, node);
        applyShapeableProperties(ctx, node);
    }

    protected void updateTextNode(@NonNull RenderContext ctx, @NonNull Text tn) {
        tn.setX(getStyledNonNull(ORIGIN_X).getConvertedValue());
        tn.setY(getStyledNonNull(ORIGIN_Y).getConvertedValue());
        applyTextFillableFigureProperties(ctx, tn);
        applyTextFontableFigureProperties(ctx, tn);
        applyTextLayoutableFigureProperties(ctx, tn);

        // We must set the font before we set the text, so that JavaFx does not
        // need to retrieve the system default font, which on Windows requires
        // that the JavaFx Toolkit is launched.
        final String text = getText(ctx);
        if (!Objects.equals(text, tn.getText())) {
            tn.setText(text);
        }
    }

}
