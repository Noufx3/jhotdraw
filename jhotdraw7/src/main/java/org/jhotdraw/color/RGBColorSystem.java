/*
 * @(#)RGBColorSystem.java
 *
 * Copyright (c) 2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.color;

/**
 * A ColorSystem for RGB color components (red, green, blue).
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class RGBColorSystem extends AbstractColorSystem {

    /**
     * Creates a new instance.
     */
    public RGBColorSystem() {
    }

    @Override
    public float[] toComponents(int r, int g, int b, float[] components) {
        if (components == null || components.length != 3) {
            components = new float[3];
        }
        components[0] = r / 255f;
        components[1] = g / 255f;
        components[2] = b / 255f;
        return components;
    }

    @Override
    public int toRGB(float... components) {
        return 0xff000000 | 
                ((int) (components[0] * 255) << 16) | 
                ((int) (components[1] * 255) << 8) | 
                (int) (components[2] * 255);
    }

    @Override
    public int getComponentCount() {
        return 3;
    }

    @Override
    public float getMinValue(int component) {
        return 0f;
    }

    @Override
    public float getMaxValue(int component) {
        return 1f;
    }
}
