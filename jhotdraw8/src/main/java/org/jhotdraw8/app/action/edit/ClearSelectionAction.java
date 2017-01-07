/*
 * @(#)ClearSelectionAction.java
 *
 * Copyright (c) 2015 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the 
 * accompanying license terms.
 */
package org.jhotdraw8.app.action.edit;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.EditableComponent;
import org.jhotdraw8.util.Resources;
import org.jhotdraw8.app.Project;

/**
 * Clears (de-selects) the selected region.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class ClearSelectionAction extends AbstractSelectionAction {

    private static final long serialVersionUID = 1L;

    public static final String ID = "edit.clearSelection";

    /**
     * Creates a new instance which acts on the currently focused component.
     *
     * @param app the application
     */
    public ClearSelectionAction(Application app) {
        this(app, null);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param app the application
     * @param target The target of the action. Specify null for the currently
     * focused component.
     */
    public ClearSelectionAction(Application app, Node target) {
        super(app, target);
        Resources.getResources("org.jhotdraw8.app.Labels").configureAction(this, ID);
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, EditableComponent c) {
        c.clearSelection();
    }
}
