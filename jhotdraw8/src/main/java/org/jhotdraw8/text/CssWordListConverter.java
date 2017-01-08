/* @(#)CssWordListConverter.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.text;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jhotdraw8.collection.ImmutableObservableList;
import org.jhotdraw8.io.IdFactory;

/**
 * CssWordListConverter.
 *
 * @author Werner Randelshofer
 */
public class CssWordListConverter implements Converter<ImmutableObservableList<String>>  {

    private final PatternConverter formatter = new PatternConverter("{0,list,{1,word}|[ ]+}", new CssConverterFactory());

    @Override
    public ImmutableObservableList<String> fromString(CharBuffer buf, IdFactory idFactory) throws ParseException, IOException {
        Object[] v = formatter.fromString(buf);
        Set<String> l = new LinkedHashSet<>((int) v[0]); // must be a set to ensure uniqueness of words
        for (int i = 0, n = (int) v[0]; i < n; i++) {
            l.add((String) v[i + 1]);
        }
        return new ImmutableObservableList<>(l);
    }

    @Override
    public ImmutableObservableList<String> getDefaultValue() {
        return ImmutableObservableList.emptyList();
    }

    @Override
    public void toString(Appendable out, IdFactory idFactory, ImmutableObservableList<String> value) throws IOException {
        Object[] v = new Object[value.size() + 1];
        v[0] = value.size();
        for (int i = 0, n = value.size(); i < n; i++) {
            v[i + 1] = value.get(i);
        }
        formatter.toString(out, v);
    }

    public String toString(List<String> value) {
        StringBuilder out = new StringBuilder();
        try {
            toString(out, value);
        } catch (IOException ex) {
            throw new InternalError(ex);
        }
        return out.toString();
    }

    public void toString(Appendable out, List<String> value) throws IOException {
        toString(out, null, value);
    }

    public void toString(Appendable out, IdFactory idFactory, List<String> value) throws IOException {
        Object[] v = new Object[value.size() + 1];
        v[0] = value.size();
        for (int i = 0, n = value.size(); i < n; i++) {
            v[i + 1] = value.get(i);
        }
        formatter.toString(out, v);
    }

}
