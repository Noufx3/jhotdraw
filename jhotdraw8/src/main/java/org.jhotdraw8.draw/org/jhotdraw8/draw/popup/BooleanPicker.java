package org.jhotdraw8.draw.popup;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.jhotdraw8.draw.DrawLabels;
import org.jhotdraw8.util.Resources;

import java.util.function.BiConsumer;

/**
 * Picker for boolean values.
 */
public class BooleanPicker extends AbstractPicker<Boolean> {
    private ContextMenu contextMenu;
    private MenuItem noneItem;
    private boolean nullable;

    public BooleanPicker(boolean nullable) {
        this.nullable = nullable;
    }


    private void init(BiConsumer<Boolean, Boolean> callback) {
        Resources labels = DrawLabels.getResources();
        contextMenu = new ContextMenu();
        MenuItem initialItem;
        MenuItem trueItem;
        MenuItem falseItem;
        initialItem = new MenuItem();
        noneItem = new MenuItem();
        trueItem = new MenuItem();
        falseItem = new MenuItem();
        initialItem.setOnAction(e -> callback.accept(false, null));
        noneItem.setOnAction(e -> callback.accept(true, null));
        trueItem.setOnAction(e -> callback.accept(true, true));
        falseItem.setOnAction(e -> callback.accept(true, false));
        labels.configureMenuItem(initialItem, "value.initial");
        labels.configureMenuItem(noneItem, "value.none");
        labels.configureMenuItem(trueItem, "value.true");
        labels.configureMenuItem(falseItem, "value.false");
        contextMenu.getItems().addAll(
                trueItem, falseItem,
                new SeparatorMenuItem(),
                initialItem, noneItem
        );
    }

    private void update(BiConsumer<Boolean, Boolean> callback) {
        init(callback);
        noneItem.setVisible(nullable);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY,
                     Boolean initialValue, BiConsumer<Boolean, Boolean> callback) {
        update(callback);
        contextMenu.show(anchor, screenX, screenY);
    }
}
