/* @(#)NumberedEditorKit.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw.
 * MIT License, CC-by License, or LGPL License.
 *
 * Original version (c) Stanislav Lapitsky
 * http://www.developer.com/java/other/article.php/3318421
 */

package org.jhotdraw.samples.teddy.text;

import javax.swing.text.*;
/**
 * NumberedEditorKit.
 * <p>
 * Usage:
 * <pre>
 * JEditorPane edit = new JEditorPane();
 * edit.setEditorKit(new NumberedEditorKit());
 * </pre>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class NumberedEditorKit extends StyledEditorKit {
    private static final long serialVersionUID = 1L;
    private NumberedViewFactory viewFactory;
    
    @Override
    public ViewFactory getViewFactory() {
        if (viewFactory == null) {
            viewFactory = new NumberedViewFactory();
        }
        return viewFactory;
    }
}