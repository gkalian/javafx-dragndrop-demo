package org.openjfx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drag'n'Drop between tables");

        // First table
        TreeItem<String> rootItem1 = new TreeItem<>("Table 1");
        rootItem1.setExpanded(true);

        ObservableList<TreeItem<String>> children1 = rootItem1.getChildren();
        children1.add(new TreeItem<>("tbl1 task 1"));
        children1.add(new TreeItem<>("tbl1 task 2"));
        children1.add(new TreeItem<>("tbl1 task 3"));
        children1.add(new TreeItem<>("tbl1 task 4"));
        children1.add(new TreeItem<>("tbl1 task5"));
        children1.add(new TreeItem<>("tbl1 task6"));
        children1.add(new TreeItem<>("tbl1 task7"));
        children1.add(new TreeItem<>("tbl1 task8"));
        TreeView<String> tree1 = new TreeView<>(rootItem1);

//        tree1.setShowRoot(false); // Hide root if needed

        // Second table
        TreeItem<String> rootItem2 = new TreeItem<>("Table 2");
        rootItem2.setExpanded(true);

        ObservableList<TreeItem<String>> children2 = rootItem2.getChildren();
        children2.add(new TreeItem<>("tbl2 task1"));
        children2.add(new TreeItem<>("tbl2 task2"));
        children2.add(new TreeItem<>("tbl2 task3"));
        children2.add(new TreeItem<>("tbl2 task4"));
        children2.add(new TreeItem<>("tbl2 task5"));
        children2.add(new TreeItem<>("tbl2 task6"));
        children2.add(new TreeItem<>("tbl2 task7"));
        children2.add(new TreeItem<>("tbl2 task8"));
        TreeView<String> tree2 = new TreeView<>(rootItem2);

//        tree2.setShowRoot(false); // Hide root if needed

        // Setting Cell Factory
        TreeCellFactory cellFactory = new TreeCellFactory();
        cellFactory.setFirstTreeView(tree1);
        tree1.setCellFactory(cellFactory);

        cellFactory.setSecondTreeView(tree2);
        tree2.setCellFactory(cellFactory);

        // Add splitter for the tables
        HBox root = new HBox(10);
        root.getChildren().addAll(tree1, tree2);

        // Setup stage
        primaryStage.setScene(new Scene(root, 500, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}