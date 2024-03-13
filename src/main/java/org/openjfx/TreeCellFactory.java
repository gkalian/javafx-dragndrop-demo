package org.openjfx;

import java.util.Objects;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class TreeCellFactory implements Callback<TreeView<String>, TreeCell<String>> {

    private TreeView<String> tree1;
    private TreeView<String> tree2;

    public TreeCellFactory() {
        // This constructor is empty
    }

    public void setFirstTreeView(TreeView<String> tree1) {
        this.tree1 = tree1;
    }

    public void setSecondTreeView(TreeView<String> tree2) {
        this.tree2 = tree2;
    }

    private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");
    private static final String DROP_HINT_STYLE = "-fx-border-color: #bcbcbc; -fx-border-width: 0 0 2 0; -fx-padding: 4 4 2 4";
    private static final Image TASKS_IMAGE = new Image("/tasks.png");
    private static final Image PIN_IMAGE = new Image("/pin.png");
    private TreeCell<String> dropZone;
    private TreeItem<String> draggedItem;

    @Override
    public TreeCell<String> call(TreeView<String> treeView) {
        TreeCell<String> cell = new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView iv = new ImageView();
                    if (item.contains("Table")) {
                        iv.setImage(TASKS_IMAGE);
                    } else {
                        iv.setImage(PIN_IMAGE);
                    }
                    setGraphic(iv);
                    setText(item);
                }

                applyCss();
                layout();
            }
        };
        cell.setOnDragDetected((MouseEvent event) -> dragDetected(event, cell));
        cell.setOnDragOver((DragEvent event) -> dragOver(event, cell, treeView));
        cell.setOnDragDropped((DragEvent event) -> dragDrop(event, cell, treeView));
        cell.setOnDragDone((DragEvent event) -> clearDropLocation());

        return cell;
    }

    private void dragDetected(MouseEvent event, TreeCell<String> treeCell) {
        draggedItem = treeCell.getTreeItem();

        if (draggedItem.getParent() == null) return;
        Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(JAVA_FORMAT, draggedItem.getValue());
        db.setContent(content);
        db.setDragView(treeCell.snapshot(null, null));
        event.consume();
    }

    private void dragOver(DragEvent event, TreeCell<String> treeCell, TreeView<String> treeView) {
        if (treeView != tree2) return;

        if (!event.getDragboard().hasContent(JAVA_FORMAT)) return;
        TreeItem<String> thisItem = treeCell.getTreeItem();

        if (draggedItem == null || thisItem == draggedItem) return;

        if (thisItem == treeView.getRoot()) {
            clearDropLocation();
            return;
        }

        event.acceptTransferModes(TransferMode.MOVE);
        if (!Objects.equals(dropZone, treeCell)) {
            clearDropLocation();
            this.dropZone = treeCell;
            dropZone.setStyle(DROP_HINT_STYLE);
        }
    }

    private void dragDrop(DragEvent event, TreeCell<String> treeCell, TreeView<String> treeView) {
        if (treeView != tree2) return;

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(JAVA_FORMAT)) return;

        TreeItem<String> thisItem = treeCell.getTreeItem();
        TreeItem<String> droppedItemParent = draggedItem.getParent();

        droppedItemParent.getChildren().remove(draggedItem);

        if (thisItem == null) {
            tree2.getRoot().getChildren().add(draggedItem);
        } else {
            int indexInParent = thisItem.getParent().getChildren().indexOf(thisItem);
            thisItem.getParent().getChildren().add(indexInParent + 1, draggedItem);
        }

        event.setDropCompleted(success);
        treeView.getSelectionModel().select(draggedItem);
    }



    private void clearDropLocation() {
        if (dropZone != null) dropZone.setStyle("");
    }

}