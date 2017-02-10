/* @(#)DockingFrameworkSample.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.samples.mini;

import java.util.function.Supplier;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jhotdraw8.gui.dock.Dock;
import org.jhotdraw8.gui.dock.DockItem;
import org.jhotdraw8.gui.dock.DockRoot;
import org.jhotdraw8.gui.dock.ScrollableVBoxTrack;
import org.jhotdraw8.gui.dock.SingleItemDock;
import org.jhotdraw8.gui.dock.SplitPaneTrack;
import org.jhotdraw8.gui.dock.TabPaneDock;
import org.jhotdraw8.gui.dock.TabbedAccordionDock;
import org.jhotdraw8.gui.dock.Track;

/**
 * DockingFrameworkSample.
 *
 * @author Werner Randelshofer
 */
public class DockingFrameworkSample extends Application {

    private int counter;

    public DockRoot initStage(Stage primaryStage, int numTabs, Supplier<Dock> dockSupp, Supplier<Track> hbSupp, Supplier<Track> vbSupp) {
        DockRoot root = new DockRoot();
        root.setDockFactory(dockSupp);
        root.setVerticalTrackFactory(vbSupp);
        root.setHorizontalTrackFactory(hbSupp);
        if (numTabs > 0) {
            final TabPaneDock tabPane = new TabPaneDock();
            for (int i = 0; i < numTabs; i++) {
                final DockItem tab = new DockItem();
                tab.setText("tab" + (++counter));
                tab.setContent(new Text("Lorem " + counter + "\nThe quick\nbrown fox\njumps over\nthe lazy\ndog."));
                tabPane.getItems().add(tab);
            }
            Track db = new SplitPaneTrack();
            db.getItems().add(tabPane);
            root.addTrack(db);
        }

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        initStage(primaryStage, 0, TabPaneDock::new, () -> new SplitPaneTrack(Orientation.HORIZONTAL), () -> new SplitPaneTrack(Orientation.VERTICAL));
        initStage(new Stage(), 3, TabPaneDock::new, () -> new SplitPaneTrack(Orientation.HORIZONTAL), () -> new SplitPaneTrack(Orientation.VERTICAL));
        DockRoot dp = initStage(new Stage(), 3, TabbedAccordionDock::new, () -> new SplitPaneTrack(Orientation.HORIZONTAL), ScrollableVBoxTrack::new);
        final TextArea textArea = new TextArea();
        dp.getRootTrack().getItems().add(new SingleItemDock(new DockItem(textArea)));
        dp.getVerticalTrackFactoryMap().put(SingleItemDock.class, () -> new SplitPaneTrack(Orientation.VERTICAL));
        //   textArea.setStyle("-fx-background-color:transparent;-fx-border-width:0,0;-fx-padding:0;");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}