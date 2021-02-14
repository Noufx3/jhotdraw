/* @(#)AbstractStyleablePropertyBeanNGTest.java
 * Copyright (c) 2016 The authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.styleable;

import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.draw.figure.FillableFigure;
import org.jhotdraw8.draw.key.NullablePaintableStyleableKey;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AbstractStyleablePropertyBeanNGTest.
 *
 * @author Werner Randelshofer
 */
public class AbstractStyleablePropertyBeanTest {

    public AbstractStyleablePropertyBeanTest() {
    }

    @Test
    public void testNullValueIsNotSameAsDefaultPropertyValue() {
        System.out.println("testNullValueIsNotSameAsDefaultPropertyValue");
        AbstractStyleablePropertyBean instance = new AbstractStyleablePropertyBeanImpl();
        final NullablePaintableStyleableKey key = FillableFigure.FILL;


        assertNotNull(key.getDefaultValue(), "need a key with a non-null default value for this test");
        assertFalse(instance.getProperties().containsKey(key), "value has not been set, map must not contain key");
        assertEquals(instance.get(key), key.getDefaultValue(), "value has not been set, must deliver default value");

        instance.set(key, null);

        assertNull(instance.get(key), "value has been explicitly set to null");
        assertTrue(instance.getProperties().containsKey(key), "map must contain key after explicit set");

        instance.remove(key);

        assertEquals(instance.get(key), key.getDefaultValue(), "key has been removed, value must be default value");
        assertFalse(instance.getProperties().containsKey(key), "key has been removed, map must not contain key");

    }

    public static class AbstractStyleablePropertyBeanImpl extends AbstractStyleablePropertyBean {

        @Override
        public @NonNull String getTypeSelector() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull String getId() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull ObservableList<String> getStyleClass() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull String getStyle() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull Styleable getStyleableParent() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public @NonNull ObservableSet<PseudoClass> getPseudoClassStates() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ObservableSet<String> getPseudoClass() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }


    }

}
