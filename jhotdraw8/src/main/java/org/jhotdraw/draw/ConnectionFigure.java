/* @(#)ConnectionFigure.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */

package org.jhotdraw.draw;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import org.jhotdraw.collection.Key;
import org.jhotdraw.draw.connector.CenterConnector;
import org.jhotdraw.draw.connector.Connector;

/**
 * A <em>connection figure</em> connects two figures with a 
 * geometric path.
 * <p>
 * The location of the start and end points of the geometric path is defined by
 * {@link Connector} objects, which are supplied by the connected figures.
 * <p>
 * If a connected figure is removed, the connection figure needs to be
 * removed as well. To achieve this, {@code ConnectionFigure} listens to
 * {@code figureRemoved} events sent by the two figures that it connects, and
 * then fires a {@code requestRemove} event to get removed as well.
 * <p>
 * The geometric path of the connection figure can be laid out using a
 * {@link Liner}.
 * <p>
 * A connection figure listens to changes in the properties of the two figures
 * that it connects. If a property is changed, the connection figure updates
 * it start, middle and end points, which may result in firing an invalidation
 * event when its {@code Node} needs to be updated.
 *
 * ConnectionFigure.
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface ConnectionFigure extends Figure {
    // ----
    // keys
    // ----

    /**
     * The start position of the geometric path.
     */
    public static Key<Point2D> START= new Key<>("start", Point2D.class, new Point2D(0,0));
    /**
     * The end position of the geometric path.
     */
    public static Key<Point2D> END = new Key<>("end", Point2D.class, new Point2D(0,0));
    /**
     * The start figure.
     * Is null if the figure is not connected at the start.
     */
    public static Key<Figure> START_FIGURE = new Key<>("startFigure", Figure.class, null);
    /**
     * The end figure.
     * Is null if the figure is not connected at the end.
     */
    public static Key<Figure> END_FIGURE = new Key<>("endFigure", Figure.class, null);
    /**
     * The start connector.
     */
    public static Key<Connector> START_CONNECTOR = new Key<>("startConnector", Connector.class, new CenterConnector());
    /**
     * The end connector.
     */
    public static Key<Connector> END_CONNECTOR = new Key<>("endConnector", Connector.class, new CenterConnector());

}