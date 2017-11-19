/* @(#)MapEntryProperty.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.collection;

import javafx.beans.binding.MapExpression;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;

/**
 * This property is weakly bound to an entry in a map.
 *
 * @author Werner Randelshofer
 * @param <K> key type
 * @param <V> map value type
 * @param <T> entry value type
 */
public class MapEntryProperty<K, V, T extends V> extends ObjectPropertyBase<T> 
implements MapChangeListener< K, V> {

    protected K key;
    protected ObservableMap<K, V> map;
    protected Class<T> tClazz;
    private WeakMapChangeListener<K, V> weakListener;

    public MapEntryProperty(ObservableMap<K, V> map, K key, Class<T> tClazz) {
        this.map = map;
        this.key = key;
        this.tClazz = tClazz;

        map.addListener(weakListener = new WeakMapChangeListener<>(this));
    }

    @Override
    public T get() {
        @SuppressWarnings("unchecked")
        T temp = (T) map.get(key);
        return temp;
    }

    @Override
    public void set(T value) {
        if (value != null && !tClazz.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("value is not assignable " + value);
        }
        V temp = (V) value;
        map.put(key, temp);

        // Note: super must be called after "put", so that listeners
        //       can be properly informed.
        super.set(value);
    }

    @Override
    public Object getBean() {
        return map;
    }

    @Override
    public String getName() {
        return key.toString();
    }

    @Override
    public void onChanged(Change<? extends K, ? extends V> change) {
 if (this.key.equals(change.getKey())) {
                if (change.wasAdded()) {// was added, or removed and then added
                    @SuppressWarnings("unchecked")
                    T valueAdded = (T) change.getValueAdded();
                    if (super.get() != valueAdded) {
                        set(valueAdded);
                    }
                } else if (change.wasRemoved()) {// was removed but not added
                    if (super.get() != null) {
                        set(null);
                    }
                }
            }    }

    @Override
    public void unbind() {
        super.unbind();
        if (map != null) {
            map.removeListener(weakListener);
            weakListener = null;
            map = null;
            key = null;
        }
    }
}
