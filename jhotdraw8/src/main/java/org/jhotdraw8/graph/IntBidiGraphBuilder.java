/* @(#)IntBidiGraphBuilder.java
 * Copyright © 2018 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * IntBidiGraphBuilder.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class IntBidiGraphBuilder extends AbstractBidiGraphBuilder {

    /**
     * Creates a builder which contains a copy of the specified graph with all
     * arrows inverted.
     *
     * @param graph a graph
     * @return a new graph with inverted arrows
     */
    @NonNull
    public static IntBidiGraphBuilder inverseOfIntBidiGraph(IntBidiGraph graph) {
        int arrowCount = graph.getArrowCount();

        IntBidiGraphBuilder b = new IntBidiGraphBuilder(graph.getVertexCount(), arrowCount);
        for (int i = 0, n = graph.getVertexCount(); i < n; i++) {
            int v = i;
            for (int j = 0, m = graph.getNextCount(v); j < m; j++) {
                b.addArrow(graph.getNext(v, j), v);
            }
        }
        return b;
    }

    /**
     * Creates a builder which contains a copy of the specified graph.
     *
     * @param graph a graph
     * @return a new graph
     */
    @NonNull
    public static IntBidiGraphBuilder ofIntBidiGraph(IntBidiGraph graph) {
        int arrowCount = graph.getArrowCount();

        IntBidiGraphBuilder b = new IntBidiGraphBuilder(graph.getVertexCount(), arrowCount);
        for (int i = 0, n = graph.getVertexCount(); i < n; i++) {
            int v = i;
            for (int j = 0, m = graph.getNextCount(v); j < m; j++) {
                b.addArrow(v, graph.getNext(v, j));
            }
        }
        return b;
    }

    public IntBidiGraphBuilder() {
        this(16, 16);
    }

    public IntBidiGraphBuilder(int vertexCapacity, int arrowCapacity) {
        super(vertexCapacity, arrowCapacity);
    }

    /**
     * Adds a directed arrow from 'a' to 'b' and another arrow from 'b' to 'a'.
     * <p>
     * Before you may call this method, you must have called
     * {@link #setVertexCount(int)}.
     *
     * @param a vertex a
     * @param b vertex b
     */
    public void addBidiArrow(int a, int b) {
        addArrow(a, b);
        addArrow(b, a);

    }

    /**
     * Adds a directed arrow from vertex 'a' to vertex 'b'.
     * <p>
     * Before you may call this method, you must have called
     * {@link #setVertexCount(int)}.
     *
     * @param a vertex a
     * @param b vertex b
     */
    public void addArrow(int a, int b) {
        buildAddArrow(a, b);
    }

    /**
     * Adds a vertex.
     */
    public void addVertex() {
        buildAddVertex();
    }

    /**
     * Builds an ImmutableIntBidiGraph from this builder.
     * 
     * @return the created graph
     */
    @NonNull
    public IntImmutableBidiGraph build() {
        return new IntImmutableBidiGraph(this);
    }

    /**
     * Sets the vertex count.
     * 
     * @param newValue the new vertex count, must be larger or equal the current vertex count.
     */
    public void setVertexCount(int newValue) {
        buildSetVertexCount(newValue);
    }

    /**
     * Removes the i-th arrow from vertex 'a'.
     *
     * @param a vertex 'a'
     * @param i the index of an arrow from vertex 'a'.
     */
    public void removeArrow(int a, int i) {
        buildRemoveArrow(a, i);
    }

}