/* @(#)DirectedGraphBuilderTest.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * DirectedGraphBuilderTest.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DirectedGraphBuilderTest {

    public DirectedGraphBuilderTest() {
    }

    /**
     * Test of buildAddArrow method, of class DirectedGraphBuilder.
     */
    @Test
    public void testBuildAddArrow() {
        System.out.println("buildAddArrow");
        int a = 0;
        int b = 1;
        int c = 1;
        DirectedGraphBuilder<Integer, Double> instance = new DirectedGraphBuilder<>();
        instance.addVertex(0);
        instance.addVertex(1);
        instance.addVertex(2);

        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 0, instance.getArrowCount());

        instance.addArrow(a, b, 1.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 1, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
    }

    /**
     * Test of getArrowCount method, of class DirectedGraphBuilder.
     */
    @Test
    public void testGetArrowCount() {
        System.out.println("getArrowCount");
        int a = 0;
        int b = 1;
        int c = 1;
        DirectedGraphBuilder<Integer, Double> instance = new DirectedGraphBuilder<Integer, Double>();
        instance.addVertex(0);
        instance.addVertex(1);
        instance.addVertex(2);

        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 0, instance.getArrowCount());

        instance.addArrow(a, b, 1.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 1, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
    }

    /**
     * Test of getNext method, of class DirectedGraphBuilder.
     */
    @Test
    public void testGetNext() {
        System.out.println("getNext");
        int a = 0;
        int b = 1;
        int c = 1;
        DirectedGraphBuilder<Integer, Double> instance = new DirectedGraphBuilder<>();
        instance.addVertex(0);
        instance.addVertex(1);
        instance.addVertex(2);

        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("arrow count", 0, instance.getArrowCount());

        instance.addArrow(a, b, 1.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("arrow count", 1, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
    }

    /**
     * Test of getNextCount method, of class DirectedGraphBuilder.
     */
    @Test
    public void testGetNextCount() {
        System.out.println("getNextCount");
        int a = 0;
        int b = 1;
        int c = 2;
        DirectedGraphBuilder<Integer, Double> instance = new DirectedGraphBuilder<>();
        instance.addVertex(0);
        instance.addVertex(1);
        instance.addVertex(2);

        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("arrow count", 0, instance.getArrowCount());
        assertEquals("next count of " + a, 0, instance.getNextCount(a));
        assertEquals("next count of " + b, 0, instance.getNextCount(b));
        assertEquals("next count of " + c, 0, instance.getNextCount(c));

        instance.addArrow(a, b, 1.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("arrow count", 1, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
        assertEquals("next count of " + b, 0, instance.getNextCount(b));
        assertEquals("next count of " + c, 0, instance.getNextCount(c));

        instance.addArrow(b, c, 2.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 2, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
        assertEquals("next count of " + b, 1, instance.getNextCount(b));
        assertEquals("next edge of " + b, c, instance.getNext(b, 0));
        assertEquals("next count of " + c, 0, instance.getNextCount(c));
    }

    /**
     * Test of getVertexCount method, of class DirectedGraphBuilder.
     */
    @Test
    public void testGetVertexCount() {
        System.out.println("getVertexCount");
        int a = 0;
        int b = 1;
        int c = 1;
        DirectedGraphBuilder<Integer, Double> instance = new DirectedGraphBuilder<>();
        instance.addVertex(0);
        instance.addVertex(1);
        instance.addVertex(2);

        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("arrow count", 0, instance.getArrowCount());

        instance.addArrow(a, b, 1.0);
        assertEquals("vertex count", 3, instance.getVertexCount());
        assertEquals("next count", 1, instance.getArrowCount());
        assertEquals("next count of " + a, 1, instance.getNextCount(a));
        assertEquals("next edge of " + a, b, instance.getNext(a, 0));
    }

}