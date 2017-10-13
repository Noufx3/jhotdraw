/* @(#)ConstantStringExpression.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ChangeListener;

/**
 * ConstantStringExpression.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class ConstantStringExpression extends StringExpression {

    private final String value;

    private ConstantStringExpression(String value) {
        this.value = value;
    }

    public static ConstantStringExpression of(String value) {
        return new ConstantStringExpression(value);
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void addListener(InvalidationListener observer) {
        // no-op
    }

    @Override
    public void addListener(ChangeListener<? super String> observer) {
        // no-op
    }

    @Override
    public void removeListener(InvalidationListener observer) {
        // no-op
    }

    @Override
    public void removeListener(ChangeListener<? super String> observer) {
        // no-op
    }
}

