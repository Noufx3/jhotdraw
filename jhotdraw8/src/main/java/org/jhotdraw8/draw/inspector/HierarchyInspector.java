/* @(#)HierarchyInspector.java
 * Copyright (c) 2016 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.inspector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import org.jhotdraw8.app.EditableComponent;
import org.jhotdraw8.collection.ExpandedTreeItemIterator;
import org.jhotdraw8.collection.ImmutableObservableList;
import org.jhotdraw8.collection.ImmutableObservableSet;
import org.jhotdraw8.draw.DrawingView;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.draw.figure.HideableFigure;
import org.jhotdraw8.draw.figure.LockableFigure;
import org.jhotdraw8.draw.figure.StyleableFigure;
import org.jhotdraw8.draw.model.DrawingModel;
import org.jhotdraw8.draw.model.DrawingModelFigureProperty;
import org.jhotdraw8.draw.model.SimpleDrawingModel;
import org.jhotdraw8.gui.BooleanPropertyCheckBoxTreeTableCell;
import org.jhotdraw8.text.CachingCollator;
import org.jhotdraw8.text.CssSetConverter;
import org.jhotdraw8.text.CssWordListConverter;
import org.jhotdraw8.text.OSXCollator;
import org.jhotdraw8.text.StringConverterAdapter;
import org.jhotdraw8.tree.SimpleTreePresentationModel;

/**
 * FXML Controller class
 *
 * @author werni
 */
public class HierarchyInspector extends AbstractDrawingViewInspector {

    @FXML
    private TreeTableColumn<Figure, ImmutableObservableList<String>> styleClassesColumn;
    @FXML
    private TreeTableColumn<Figure, ImmutableObservableSet<PseudoClass>> pseudoClassesColumn;
    private final CachingCollator collator = new CachingCollator(new OSXCollator());

    private DrawingView drawingView;
    @FXML
    private TreeTableColumn<Figure, String> idColumn;
    private boolean isUpdatingSelectionInView;
    @FXML
    private TreeTableColumn<Figure, Boolean> lockedColumn;
    private SimpleTreePresentationModel<Figure> model;
    private Node node;
    private final InvalidationListener treeSelectionHandler = change -> {
        if (model.isUpdating()) {
//        updateSelectionInTree();
        } else {
            updateSelectionInView();
        }
    };
    @FXML
    private TreeTableView<Figure> treeView;
    @FXML
    private TreeTableColumn<Figure, String> typeColumn;
    private final SetChangeListener<Figure> viewSelectionHandler = this::updateSelectionInTreeLater;
    @FXML
    private TreeTableColumn<Figure, Boolean> visibleColumn;
    private boolean willUpdateSelectionInTree;

    private CssWordListConverter wordListConverter = new CssWordListConverter();

    public HierarchyInspector() {
        this(HierarchyInspector.class.getResource("HierarchyInspector.fxml"),
                Labels.getBundle());
    }

    public HierarchyInspector(URL fxmlUrl, ResourceBundle resources) {
        init(fxmlUrl, resources);
    }

    @Override
    public Node getNode() {
        return node;
    }

    private void init(URL fxmlUrl, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setResources(resources);
        try (InputStream in = fxmlUrl.openStream()) {
            node = loader.load(in);
        } catch (IOException ex) {
            throw new InternalError(ex);
        }

        model = new SimpleTreePresentationModel<>();
        typeColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(
                cell.getValue().getValue() == null ? null : cell.getValue().getValue().getTypeSelector())
        );
        idColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<String>((DrawingModel) model.getTreeModel(),
                        cell.getValue().getValue(), StyleableFigure.ID) {
            @Override
            public String getValue() {
                return figure == null ? null : figure.get(StyleableFigure.ID);
            }

            @Override
            protected void updateValue() {
                setValue(figure.getId());
            }
        }
        );
        visibleColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<Boolean>((DrawingModel) model.getTreeModel(),
                        cell.getValue().getValue(), HideableFigure.VISIBLE)
        );
        lockedColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<Boolean>((DrawingModel) model.getTreeModel(),
                        cell.getValue().getValue(), LockableFigure.LOCKED)
        );
        styleClassesColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<ImmutableObservableList<String>>((DrawingModel) model.getTreeModel(),
                        cell.getValue().getValue(), StyleableFigure.STYLE_CLASS) {
            @Override
            @SuppressWarnings("unchecked")
            public ImmutableObservableList<String> getValue() {
                return figure == null ? null : new ImmutableObservableList<>(figure.getStyleClass());
            }
        }
        );
        pseudoClassesColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<ImmutableObservableSet<PseudoClass>>((DrawingModel) model.getTreeModel(),
                        cell.getValue().getValue(), StyleableFigure.PSEUDO_CLASS_STATES) {
            @Override
            @SuppressWarnings("unchecked")
            public ImmutableObservableSet<PseudoClass> getValue() {
                return figure == null ? null : new ImmutableObservableSet<>(figure.getPseudoClassStates());
            }
        }
        );

        // This cell factory ensures that only styleable figures support editing of ids.
        // And it ensures, that the users sees the computed id, and not the one that he entered. 
        idColumn.setCellFactory(
                new Callback<TreeTableColumn<Figure, String>, TreeTableCell<Figure, String>>() {

            @Override
            public TreeTableCell<Figure, String> call(TreeTableColumn<Figure, String> paramTableColumn) {
                return new TextFieldTreeTableCell<Figure, String>(new DefaultStringConverter()) {
                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        updateItem(getItem(), false);
                    }

                    @Override
                    public void updateItem(String t, boolean empty) {
                        super.updateItem(t, empty);
                        TreeTableRow<Figure> row = getTreeTableRow();
                        boolean isEditable = false;
                        if (row != null) {
                            Figure item = row.getItem();
                            //Test for disable condition
                            if (item != null && item.isSupportedKey(StyleableFigure.ID)) {
                                isEditable = true;
                            }

                            // show the computed  id!
                            if (item != null) {
                                setText(item.getId());
                            }
                        }
                        if (isEditable) {
                            setEditable(true);
                            this.setStyle(null);
                        } else {
                            setEditable(false);
                            this.setStyle("-fx-text-fill: grey");
                        }
                    }
                };
            }

        });
        // This cell factory ensures that only styleable figures support editing of style classes.
        // And it ensures, that the users sees the computed style classes, and not the ones that he entered. 
        // And it ensures, that the synthetic synthetic style classes are not stored in the STYLE_CLASSES attribute.
        styleClassesColumn.setCellFactory(new Callback<TreeTableColumn<Figure, ImmutableObservableList<String>>, TreeTableCell<Figure, ImmutableObservableList<String>>>() {

            @Override
            public TreeTableCell<Figure, ImmutableObservableList<String>> call(TreeTableColumn<Figure, ImmutableObservableList<String>> paramTableColumn) {
                return new TextFieldTreeTableCell<Figure, ImmutableObservableList<String>>() {
                    {
                        setConverter(new StringConverterAdapter<>(wordListConverter));
                    }

                    private Set<String> syntheticClasses = new HashSet<>();

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        updateItem(getItem(), false);
                        syntheticClasses.clear();
                    }

                    @Override
                    public void commitEdit(ImmutableObservableList<String> newValue) {
                        LinkedHashSet<String> newValueSet = new LinkedHashSet<>(newValue);
                        newValueSet.removeAll(syntheticClasses);
                        super.commitEdit(new ImmutableObservableList<>(newValueSet));
                    }

                    @Override
                    public void startEdit() {
                        Figure figure = getTreeTableRow().getItem();
                        figure.get(StyleableFigure.STYLE_CLASS);
                        syntheticClasses.clear();
                        syntheticClasses.addAll(figure.getStyleClass());
                        syntheticClasses.removeAll(figure.get(StyleableFigure.STYLE_CLASS));
                        super.startEdit();
                    }

                    @Override
                    public void updateItem(ImmutableObservableList<String> t, boolean empty) {
                        super.updateItem(t, empty);
                        TreeTableRow<Figure> row = getTreeTableRow();
                        boolean isEditable = false;
                        if (row != null) {
                            Figure figure = row.getItem();
                            //Test for disable condition
                            if (figure != null && figure.isSupportedKey(StyleableFigure.STYLE_CLASS)) {
                                isEditable = true;
                            }
                            // show the computed  classes! 
                            if (figure != null) {
                                setText(wordListConverter.toString(new ImmutableObservableList<>(figure.getStyleClass())));
                            }
                        }
                        if (isEditable) {
                            setEditable(true);
                            this.setStyle(null);
                        } else {
                            setEditable(false);
                            this.setStyle("-fx-text-fill: grey");
                        }
                    }
                };
            }
        });
        CssSetConverter<PseudoClass> pseudoClassConverter = new CssSetConverter<>();
        pseudoClassesColumn.setCellFactory(new Callback<TreeTableColumn<Figure, ImmutableObservableSet<PseudoClass>>, TreeTableCell<Figure, ImmutableObservableSet<PseudoClass>>>() {
            @Override
            public TreeTableCell<Figure, ImmutableObservableSet<PseudoClass>> call(TreeTableColumn<Figure, ImmutableObservableSet<PseudoClass>> paramTableColumn) {
                return new TextFieldTreeTableCell<Figure, ImmutableObservableSet<PseudoClass>>() {
                    {
                        setConverter(new StringConverterAdapter<>(pseudoClassConverter));
                    }

                };
            }
        });

        final Comparator<String> comparator = (a, b) -> collator.compare(a, b);
        typeColumn.setComparator(comparator);
        idColumn.setComparator(comparator);
        //classesColumn.setComparator(comparator);

        visibleColumn.setCellFactory(BooleanPropertyCheckBoxTreeTableCell.forTreeTableColumn());
        lockedColumn.setCellFactory(BooleanPropertyCheckBoxTreeTableCell.forTreeTableColumn());
        treeView.setRoot(model.getRoot());
        model.getRoot().setExpanded(true);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getSelectionModel().getSelectedCells().addListener(treeSelectionHandler);

        treeView.setRowFactory(tv -> {
            TreeTableRow<Figure> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Figure rowData = row.getItem();
                    drawingView.scrollFigureToVisible(rowData);
                }
            });
            return row;
        });
    }

    @Override
    protected void onDrawingViewChanged(DrawingView oldValue, DrawingView newValue) {
        if (oldValue != null) {
            oldValue.getSelectedFigures().removeListener(viewSelectionHandler);
            treeView.getProperties().put(EditableComponent.EDITABLE_COMPONENT, null);
        }
        drawingView = newValue;
        if (newValue != null) {
            model.setTreeModel(newValue.getModel());
            newValue.getSelectedFigures().addListener(viewSelectionHandler);
            treeView.getProperties().put(EditableComponent.EDITABLE_COMPONENT, drawingView);
        } else {
            model.setTreeModel(new SimpleDrawingModel());
        }
    }

    private void updateSelectionInTree() {
        willUpdateSelectionInTree = false;
        if (!isUpdatingSelectionInView) {
            isUpdatingSelectionInView = true;
            TreeTableView.TreeTableViewSelectionModel<Figure> selectionModel = treeView.getSelectionModel();
            // Performance: collecting all indices and then setting them all at once is 
            // much faster than invoking selectionModel.select(Object) for each item.
            Set<Figure> selection = drawingView.getSelectedFigures();
            switch (selection.size()) {
                case 0:
                    selectionModel.clearSelection();
                    break;
                case 1:
                    selectionModel.clearSelection();
                    selectionModel.select(model.getTreeItem(selection.iterator().next()));
                    break;
                default:
                    int index = 0;
                    int count = 0;
                    final int size = selection.size();
                    for (TreeItem<Figure> node : (Iterable<TreeItem<Figure>>) () -> new ExpandedTreeItemIterator<>(model.getRoot())) {
                        boolean isSelected = selection.contains(node.getValue());
                        if (isSelected != selectionModel.isSelected(index)) {
                            if (isSelected) {
                                selectionModel.select(index);
                            } else {
                                selectionModel.clearSelection(index);
                            }
                        }
                        if (isSelected && ++count == size) {
                            break;
                        }
                        index++;
                    }
            }
            isUpdatingSelectionInView = false;
        }
    }

    private void updateSelectionInTreeLater(SetChangeListener.Change<? extends Figure> change) {
        if (!willUpdateSelectionInTree && !isUpdatingSelectionInView) {
            willUpdateSelectionInTree = true;
            Platform.runLater(this::updateSelectionInTree);
        }
    }

    private void updateSelectionInView() {
        if (!isUpdatingSelectionInView) {
            isUpdatingSelectionInView = true;
            TreeTableView.TreeTableViewSelectionModel<Figure> selectionModel = treeView.getSelectionModel();
            Set<Figure> newSelection = new LinkedHashSet<>();
            for (TreeItem<Figure> item : selectionModel.getSelectedItems()) {
                if (item != null) {
                    newSelection.add(item.getValue());
                }
            }
            drawingView.getSelectedFigures().retainAll(newSelection);
            drawingView.getSelectedFigures().addAll(newSelection);
            isUpdatingSelectionInView = false;
        }
    }

}
