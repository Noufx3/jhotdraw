/* @(#)GroupFigure.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jhotdraw.draw.figure.AbstractCompositeFigure;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.jhotdraw.draw.RenderContext;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.geom.Geom;

/**
 * A figure which groups child figures, so that they can be edited by the user
 * as a unit.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class GroupFigure extends AbstractCompositeFigure implements TransformableFigure, HideableFigure, StyleableFigure, LockableFigure {

    /**
     * The CSS type selector for group objects is @code("group"}.
     */
    public final static String TYPE_SELECTOR = "Group";

    @Override
    public void reshape(Transform transform) {
        for (Figure child : getChildren()) {
            Transform p2c = child.getParentToLocal();
            if (p2c.isIdentity()) {
                child.reshape(transform);
            } else if (child instanceof TransformableFigure) {
                child.set(TRANSFORM, Collections.singletonList(child.getLocalToParent()));
                child.set(ROTATE, 0.0);
                child.set(TRANSLATE_X, 0.0);
                child.set(TRANSLATE_Y, 0.0);
                child.set(SCALE_X, 1.0);
                child.set(SCALE_Y, 1.0);

                if (transform instanceof Translate) {
                    Point2D tr = new Point2D(transform.getTx(), transform.getTy());
                    tr = Geom.toDeltaTransform(p2c).transform(tr);
                    Transform t = new Translate(tr.getX(), tr.getY());
                    child.reshape(t);
                } else {
                    child.reshape(p2c.createConcatenation(transform));
                }
            }
        }
    }

    @Override
    public void updateNode(RenderContext ctx, Node n) {
        applyHideableFigureProperties(n);
        applyTransformableFigureProperties(n);
        applyStyleableFigureProperties(ctx, n);

        List<Node> nodes = new ArrayList<Node>(getChildren().size());
        for (Figure child : getChildren()) {
            nodes.add(ctx.getNode(child));
        }
        ObservableList<Node> group = ((Group) n).getChildren();
        if (!group.equals(nodes)) {
            group.setAll(nodes);
        }
    }

    @Override
    public Node createNode(RenderContext drawingView) {
        Group g = new Group();
        g.setAutoSizeChildren(false);
        return g;
    }

    /**
     * Returns false.
     */
    @Override
    public boolean isLayoutable() {
        return false;
    }

    @Override
    public Connector findConnector(Point2D p, Figure prototype) {
        return null;
    }

    @Override
    public String getTypeSelector() {
        return TYPE_SELECTOR;
    }
}