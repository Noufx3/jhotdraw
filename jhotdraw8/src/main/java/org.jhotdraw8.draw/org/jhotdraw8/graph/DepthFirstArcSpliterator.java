/*
 * @(#)DepthFirstSpliterator.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.AbstractEnumeratorSpliterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * DepthFirstSpliterator.
 *
 * @param <V> the vertex type
 * @author Werner Randelshofer
 */
public class DepthFirstArcSpliterator<V, A> extends AbstractEnumeratorSpliterator<Arc<V, A>> {

    @NonNull
    private final Function<V, Iterable<Arc<V, A>>> nextFunction;
    @NonNull
    private final Deque<Arc<V, A>> deque;
    @NonNull
    private final Predicate<Arc<V, A>> visited;

    /**
     * Creates a new instance.
     *
     * @param nextArcsFunction the nextFunction
     * @param root             the root vertex
     */
    public DepthFirstArcSpliterator(Function<V, Iterable<Arc<V, A>>> nextArcsFunction, V root) {
        this(nextArcsFunction, root, new HashSet<>()::add);
    }

    /**
     * Creates a new instance.
     *
     * @param nextFunction the function that returns the next vertices of a given vertex
     * @param root         the root vertex
     * @param visited      a predicate with side effect. The predicate returns true
     *                     if the specified vertex has been visited, and marks the specified vertex
     *                     as visited.
     */
    public DepthFirstArcSpliterator(@Nullable Function<V, Iterable<Arc<V, A>>> nextFunction, @Nullable V root, @Nullable Predicate<Arc<V, A>> visited) {
        super(Long.MAX_VALUE, ORDERED | DISTINCT | NONNULL);
        Objects.requireNonNull(nextFunction, "nextFunction is null");
        Objects.requireNonNull(root, "root is null");
        Objects.requireNonNull(visited, "visited is null");
        this.nextFunction = nextFunction;
        deque = new ArrayDeque<>(16);
        this.visited = visited;
        Arc<V, A> rootArc = new Arc<>(null, root, null);
        deque.push(rootArc);
        visited.test(rootArc);
    }

    @Override
    public boolean moveNext() {
        current = deque.pollLast();
        if (current == null) {
            return false;
        }
        for (Arc<V, A> next : nextFunction.apply(current.getEnd())) {
            if (visited.test(next)) {
                deque.addLast(next);
            }
        }
        return true;
    }
}