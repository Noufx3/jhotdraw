/* @(#)CssDoubleConverter.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.text;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.ParseException;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.css.CssTokenizerInterface;
import org.jhotdraw8.io.DefaultUnitConverter;
import org.jhotdraw8.io.IdFactory;
import org.jhotdraw8.io.SimpleIdFactory;

/**
 * CssDoubleConverter.
 * <p>
 * Parses the following EBNF from the
 * <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html">JavaFX
 * CSS Reference Guide</a>.
 * </p>
 * <pre>
 * Size := Double, [Unit] ;
 * Unit := ("px"|"mm"|"cm"|in"|"pt"|"pc"]"em"|"ex") ;
 * </pre>
 *
 * // FIXME should return a Size object and not just a Double.
 *
 * @author Werner Randelshofer
 */
public class CssDoubleConverter implements Converter<Double> {

    private final  CssSizeConverter sizeConverter ;

    public CssDoubleConverter() {
        this(false);
    }

    public CssDoubleConverter(boolean nullable) {
        sizeConverter =new CssSizeConverter(nullable);
    }


    @Override
    public Double fromString(CharBuffer buf, IdFactory idFactory) throws ParseException, IOException {
        CssSize size=sizeConverter.fromString(buf,idFactory);
        return size==null?null:(idFactory!=null?  idFactory.convert(size.getValue(),size.getUnits(), "px"):
                DefaultUnitConverter.getInstance().convert(size.getValue(),size.getUnits(), "px"));
    }

    @Override
    public Double getDefaultValue() {
        return 0.0;
    }
    @Override
    public void toString(Appendable out, IdFactory idFactory, Double value) throws IOException {
        CssSize size=value==null?null:new CssSize(value,null);
        sizeConverter.toString(out,idFactory,size);
    }
}