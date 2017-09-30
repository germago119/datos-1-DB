package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONInstance;
import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import LinkedDB.Lists.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

/**
 * This class defines the Node for the Documents on the TreeView.
 *
 * @author Roger Valderrama
 */

public class TreeDocumentNode extends TreeAbstractNode {

    private JSONDocument document;
    private JSONStore store;

    public TreeDocumentNode(JSONDocument document, String store) {
        this.setValue(document.getName());
        this.document = document;
        this.store = ListUI.findStore(store);
    }

    @Override
    public ContextMenu getMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem addInstance = new MenuItem("Add Object");
        addInstance.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (document.findPrimary() != null) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartInstance.fxml"));
                        Parent root = loader.load();
                        NewInstanceController controller = loader.getController();
                        controller.initialize(document.getAttributes());
                        controller.setStore(store.getName());
                        controller.setDocument(document.getName());
                        controller.setDocument();
                        Stage stage = new Stage();
                        stage.setTitle("Create a new Instance");
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.showAndWait();
                    } else {
                        Alert invalidName = new Alert(AlertType.WARNING);
                        invalidName.setTitle("WARNING");
                        invalidName.setHeaderText("Primary atribute missing");
                        invalidName.setContentText("The document must have a primary key assigned to an atribute "
                                + "in order to create an instance");
                        invalidName.showAndWait();
                    }
                } catch (IOException ex) {

                }
            }
        });
        MenuItem addAttribute = new MenuItem("Add Attribute");
        addAttribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (document.getInstances().getHead() != null) {
                    Alert invalidName = new Alert(AlertType.WARNING);
                    invalidName.setTitle("WARNING");
                    invalidName.setHeaderText("Existing Instances");
                    invalidName.setContentText("You can't add a new atribute to this document because there are"
                            + " already existing instances with a different structure");
                    invalidName.showAndWait();
                    return;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("StartDocumentDesign.fxml"));
                    Parent root = loader.load();
                    DocumentDesignController controller = loader.getController();
                    controller.setStore(store.getName());
                    controller.setDocument(document.getName());
                    Stage stage = new Stage();
                    stage.setTitle("Create a new Atribute");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.showAndWait();

                } catch (IOException ex) {

                }
            }
        });
        MenuItem deleteDocument = new MenuItem("Delete");
        deleteDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node<JSONInstance> instances = document.getInstances().getHead();
                document.updateReferences();
                while (instances != null) {
                    if (document.deletePrimaryKey(instances.getValue().getName())[0]) {
                    }
                    instances = instances.getNext();
                }
                if (!document.hasReferences()) {
                    ListUI.deleteDocument(document, store);
                }
                HEADController.loadTree();
            }
        });
        MenuItem deleteInstances = new MenuItem("Delete All Objects");
        deleteInstances.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                document.updateReferences();
                Node<JSONInstance> instances = document.getInstances().getHead();
                while (instances != null) {
                    document.deletePrimaryKey(instances.getValue().getName());
                    instances = instances.getNext();
                }
                HEADController.loadTree();
            }
        });
        MenuItem deleteKey = new MenuItem("Delete By Key");
        deleteKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Delete Document");
                dialog.setHeaderText("Delete by Key");
                dialog.setContentText("Please enter the name (key) of the object you want to delete:");

                Optional<String> resultInput = dialog.showAndWait();
                if (resultInput.isPresent()) {
                    if (!document.deletePrimaryKey(resultInput.get())[1]) {
                        Alert invalidPath = new Alert(AlertType.ERROR);
                        invalidPath.setTitle("ERROR");
                        invalidPath.setHeaderText("Invalid Name (key)");
                        invalidPath.setContentText("There's no object with that name (key)");
                        invalidPath.showAndWait();
                    }
                }
                HEADController.loadTree();
            }
        });
        MenuItem searchAtribute = new MenuItem("Search By Atribute");
        searchAtribute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Search");
                dialog.setHeaderText("Search by Atribute.\n\nIf not specified, all matching values will be searched,\nregardless of their atribute.");
                ButtonType searchButton = new ButtonType("Search", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(searchButton, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField atribute = new TextField();
                atribute.setPromptText("Atribute's Name");
                TextField value = new TextField();
                value.setPromptText("Atribute's Value");
                grid.add(new Label("Atribute:"), 0, 1);
                grid.add(atribute, 1, 1);
                grid.add(new Label("Value:"), 0, 2);
                grid.add(value, 1, 2);

                dialog.getDialogPane().setContent(grid);
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == searchButton) {
                        return new Pair<>(atribute.getText(), value.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                if (!result.isPresent()) {
                    return;
                }

                if (!result.get().getValue().equals("")) {
                    if (result.get().getKey().equals("")) {
                        HEADController.setDataLabel("These are all the current objects inside the " + document.getName() + " document that have " + result.get().getValue() + " as an Atribute");
                        HEADController.loadTable(document, document.searchByAttribute(result.get().getValue(), null));
                    } else {
                        HEADController.setDataLabel("These are all the current objects inside the " + document.getName() + " documentument that have " + result.get().getValue() + " as " + result.get().getKey());
                        HEADController.loadTable(document, document.searchByAttribute(result.get().getValue(), result.get().getKey()));
                    }
                } else {
                    Alert emptySlots = new Alert(AlertType.WARNING);
                    emptySlots.setTitle("WARNING");
                    emptySlots.setHeaderText("Empty Slots");
                    emptySlots.setContentText("At least the value field must be filled");
                    emptySlots.showAndWait();
                    handle(event);
                }

            }
        });
        MenuItem showObjects = new MenuItem("Show All Objects");
        showObjects.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node<JSONInstance> instance = document.getInstances().getHead();
                ObservableList<JSONInstance> result = FXCollections.observableArrayList();

                while (instance != null) {
                    result.add(instance.getValue());
                    instance = instance.getNext();
                }
                HEADController.loadTable(document, result);
                HEADController.setDataLabel("These are all the current objects inside the " + document.getName() + " document");
            }
        });
        MenuItem updateObjects = new MenuItem("Update Objects");
        updateObjects.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<String[]> dialog = new Dialog<>();
                dialog.setTitle("Update");
                dialog.setHeaderText("Update by Atribute.\n\nChanges the value of the atribute to update, for the new value in all\n"
                        + "the objects that have the value to search in the atribute to search.");
                ButtonType updateButton = new ButtonType("Update", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField updateAtribute = new TextField();
                updateAtribute.setPromptText("Atribute to update");

                TextField newValue = new TextField();
                newValue.setPromptText("New value");

                TextField lookAtribute = new TextField();
                lookAtribute.setPromptText("Atribute to search");

                TextField lookValue = new TextField();
                lookValue.setPromptText("Value to search");

                grid.add(new Label("Atribute to update :"), 0, 0);
                grid.add(updateAtribute, 1, 0);
                grid.add(new Label("Atribute to search"), 0, 2);
                grid.add(lookAtribute, 1, 2);

                grid.add(new Label("New value : "), 0, 1);
                grid.add(newValue, 1, 1);
                grid.add(new Label("Value to search :"), 0, 3);
                grid.add(lookValue, 1, 3);

                dialog.getDialogPane().setContent(grid);
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButton) {
                        String[] result = {updateAtribute.getText(), newValue.getText(), lookAtribute.getText(),
                                lookValue.getText()};
                        return result;
                    }
                    return null;
                });

                Optional<String[]> result = dialog.showAndWait();

                if (!result.isPresent()) {
                    return;
                }

                if (!(result.get()[1].equals("") || result.get()[0].equals("") || result.get()[2].equals("") || result.get()[3].equals(""))) {
                    document.updateAttribute(result.get()[0], result.get()[1], result.get()[2], result.get()[3]);
                    HEADController.loadTree();
                } else {
                    Alert emptySlots = new Alert(AlertType.WARNING);
                    emptySlots.setTitle("WARNING");
                    emptySlots.setHeaderText("Empty Slots");
                    emptySlots.setContentText("At least the value field must be filled");
                    emptySlots.showAndWait();
                    handle(event);
                }
            }
        });
        menu.getItems().addAll(addInstance, addAttribute, deleteDocument, deleteInstances,
                deleteKey, searchAtribute, showObjects, updateObjects);
        return menu;
    }

}