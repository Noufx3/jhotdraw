/* @(#)TogglePropertiesPanelAction.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw.
 * MIT License, CC-by License, or LGPL License.
 */

package org.jhotdraw.samples.odg.action;

import javax.annotation.Nullable;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import org.jhotdraw.app.*;
import org.jhotdraw.app.action.*;
import org.jhotdraw.samples.odg.*;
import org.jhotdraw.util.*;

/**
 * TogglePropertiesPanelAction.
 * 
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TogglePropertiesPanelAction extends AbstractViewAction {
        private static final long serialVersionUID = 1L;

    /** Creates a new instance. */
    public TogglePropertiesPanelAction(Application app, @Nullable View view) {
        super(app, view);
        setPropertyName("propertiesPanelVisible");
        ResourceBundleUtil labels = new ResourceBundleUtil(ResourceBundle.getBundle("org.jhotdraw.samples.odg.Labels"));
        putValue(AbstractAction.NAME, labels.getString("propertiesPanel"));
    }
    
    /**
     * This method is invoked, when the property changed and when
     * the view changed.
     */
    @Override
    protected void updateView() {
        putValue(ActionUtil.SELECTED_KEY,
                getActiveView() != null &&
                ! getActiveView().isPropertiesPanelVisible()
                );
    }
    
    
    @Override
    public ODGView getActiveView() {
        return (ODGView) super.getActiveView();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        getActiveView().setPropertiesPanelVisible(
                ! getActiveView().isPropertiesPanelVisible()
                );
    }
    
}