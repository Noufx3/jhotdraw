/* @(#)DumpGraphs.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jhotdraw8.collection.IntArrayList;

/**
 * Provides algorithms for directed graphs.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DumpGraphs {

    /**
     * Dumps the graph for debugging purposes.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param graph the graph to be dumped
     * @return the dump
     */
    public static <V, A> String dumpAsAdjacencyList(@NonNull DirectedGraph<V, A> graph) {
        StringWriter w = new StringWriter();
        try {
            dumpAsAdjacencyList(w, graph, Object::toString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return w.toString();
    }

    /**
     * Dumps the graph for debugging purposes.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param w the writer
     * @param graph the graph to be dumped
     * @throws java.io.IOException if writing fails
     */
    public static <V, A> void dumpAsAdjacencyList(@NonNull Appendable w, @NonNull DirectedGraph<V, A> graph) throws IOException {
        dumpAsAdjacencyList(w, graph, Object::toString);
    }

    /**
     * Dumps the graph for debugging purposes.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param w the writer
     * @param graph the graph to be dumped
     * @param toStringFunction a function which converts a vertex to a string
     * @throws java.io.IOException if writing fails
     */
    public static <V, A> void dumpAsAdjacencyList(@NonNull Appendable w, DirectedGraph<V, A> graph, @NonNull Function<V, String> toStringFunction) throws IOException {
        for (int i = 0, nn = graph.getVertexCount(); i < nn; i++) {
            V v = graph.getVertex(i);
            if (i != 0) {
                w.append("\n");
            }
            w.append(toStringFunction.apply(v)).append(" -> ");
            for (int j = 0, n = graph.getNextCount(v); j < n; j++) {
                if (j != 0) {
                    w.append(", ");
                }
                w.append(toStringFunction.apply(graph.getNext(v, j)));
            }
            w.append('.');
        }
    }

    /**
     * Dumps the graph graph into a String which can be rendered with the "dot"
     * tool.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param graph the graph to be dumped
     * @return the dump
     */
    public static <V, A> String dumpAsDot(@NonNull DirectedGraph<V, A> graph) {
        StringWriter w = new StringWriter();
        try {
            dumpAsDot(w, graph, Object::toString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return w.toString();
    }

    /**
     * Dumps a directed graph into a String which can be rendered with the "dot"
     * tool.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param w the writer
     * @param graph the graph
     * @throws java.io.IOException if writing fails
     */
    public static <V, A> void dumpAsDot(@NonNull Appendable w, @NonNull DirectedGraph<V, A> graph) throws IOException {
        dumpAsDot(w, graph, v -> "\"" + v + '"', null, null);
    }

    /**
     * Dumps a directed graph into a String which can be rendered with the "dot"
     * tool.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param w the writer
     * @param graph the graph
     * @param vertexToString a function that converts a vertex to a String for
     * use as vertex name
     * @throws java.io.IOException if writing fails
     */
    public static <V, A> void dumpAsDot(@NonNull Appendable w, @NonNull DirectedGraph<V, A> graph,
            @NonNull Function<V, String> vertexToString) throws IOException {
        dumpAsDot(w, graph, vertexToString, null, null);
    }

    /**
     * Dumps a directed graph into a String which can be rendered with the "dot"
     * tool.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param graph the graph
     * @param vertexToString a function that converts a vertex to a String for
     * use as vertex name
     * @param vertexAttributes a function that converts a vertex to a String for
     * use as vertex attributes
     * @param arrowAttributes a function that converts an arrow to a String for
     * use as arrow attributes
     * @return the "dot" string
     */
    public static <V, A> String dumpAsDot(@NonNull DirectedGraph<V, A> graph,
            @NonNull Function<V, String> vertexToString,
            Function<V, String> vertexAttributes,
            Function<A, String> arrowAttributes) {
        StringWriter w = new StringWriter();
        try {
            dumpAsDot(w, graph, vertexToString, vertexAttributes, arrowAttributes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return w.toString();
    }

    /**
     * Dumps a directed graph into a String which can be rendered with the "dot"
     * tool.
     *
     * @param <V> the vertex type
     * @param <A> the arrow type
     * @param w the writer
     * @param graph the graph
     * @param vertexToString a function that converts a vertex to a String for
     * use as vertex name
     * @param vertexAttributes a function that converts a vertex to a String for
     * use as vertex attributes
     * @param arrowAttributes a function that converts an arrow to a String for
     * use as arrow attributes
     * @throws java.io.IOException if writing fails
     */
    public static <V, A> void dumpAsDot(@NonNull final Appendable w,
            @NonNull final DirectedGraph<V, A> graph,
            @NonNull final Function<V, String> vertexToString,
            @Nullable final Function<V, String> vertexAttributes,
            @Nullable final Function<A, String> arrowAttributes) throws IOException {
        w.append("digraph G {\n");

        // dump vertices
        for (int i = 0, n = graph.getVertexCount(); i < n; i++) {
            final V v = graph.getVertex(i);
            final String vertexName = vertexToString.apply(v);
            if (vertexName == null) {
                continue;
            }
            final String vattr = vertexAttributes == null ? null : vertexAttributes.apply(v);
            if (graph.getNextCount(v) == 0 || vattr != null && !vattr.isEmpty()) {
                w.append(vertexName);
                if (vattr != null && !vattr.isEmpty()) {
                    w.append(" [").append(vattr).append("]");
                }
                //w.append(";");
                w.append('\n');
            }
        }

        // dump arrows
        for (int i = 0, n = graph.getVertexCount(); i < n; i++) {
            final V start = graph.getVertex(i);
            for (int j = 0, m = graph.getNextCount(start); j < m; j++) {
                final V end = graph.getNext(start, j);
                final A arrow = graph.getNextArrow(start, j);
                final String startVertexName = vertexToString.apply(start);
                final String endVertexName = vertexToString.apply(end);
                if (startVertexName == null || endVertexName == null) {
                    continue;
                }
                w.append(startVertexName);
                w.append(" -> ")
                        .append(endVertexName);
                if (arrowAttributes != null) {
                    final String attrString = arrowAttributes.apply(arrow);
                    if (attrString != null && !attrString.isEmpty()) {
                        w.append(" [");
                        w.append(attrString);
                        w.append("]");
                    }
                    //w.append(";");
                }
                w.append('\n');
            }
        }

        w.append("}\n");
    }


    /**
     * Prevents instance creation.
     */
    private DumpGraphs() {
    }

}