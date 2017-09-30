package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONInstance;
import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import LinkedDB.Lists.List;
import LinkedDB.Lists.Node;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

/**
 * This class defines the controller for the main Window.
 *
 * @author Roger Valderrama
 */

public class Controller {

    @FXML
    private MenuItem addStoreButton;

    @FXML
    private MenuItem addDocumentButton;

    @FXML
    private MenuItem addAttributeButton;

    @FXML
    private MenuItem directoryButton;

    @FXML
    private ImageView addStorePNG;

    @FXML
    private ImageView addDocumentPNG;

    @FXML
    private ImageView addAttributePNG;

    @FXML
    private ImageView newInstancePNG;

    @FXML
    private ImageView searchPNG;

    @FXML
    private ImageView deletePNG;

    @FXML
    private ImageView commitPNG;

    @FXML
    private TableView<JSONInstance> tableView;

    @FXML
    private TreeView<String> tree;

    @FXML
    private Label showDataLabel;


    @FXML
    public void createStore(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StartStore.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create a new Store");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    public void createDocument(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StartDocument.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create a new Document");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    public void createAttribute(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StartDocumentDesign.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create a new Attribute");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    public void createInstance(JSONDocument document, JSONStore store) {
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
                Alert invalidName = new Alert(Alert.AlertType.WARNING);
                invalidName.setTitle("WARNING");
                invalidName.setHeaderText("Primary atribute missing");
                invalidName.setContentText("The document must have a primary key assigned to an atribute "
                        + "in order to create an instance");
                invalidName.showAndWait();
            }
        } catch (IOException ex) {
        }

    }

    @FXML
    public void searchObject(JSONDocument document) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Search");
        dialog.setHeaderText("Search by Atribute.\n\nIf not specified, all matching values will be searched,\nregardless of their atribute.");
        ButtonType searchButton2 = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButton2, ButtonType.CANCEL);

        GridPane grid2 = new GridPane();
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(20, 150, 10, 10));

        TextField atribute = new TextField();
        atribute.setPromptText("Atribute's Name");
        TextField value = new TextField();
        value.setPromptText("Atribute's Value");
        grid2.add(new Label("Atribute:"), 0, 1);
        grid2.add(atribute, 1, 1);
        grid2.add(new Label("Value:"), 0, 2);
        grid2.add(value, 1, 2);

        dialog.getDialogPane().setContent(grid2);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButton2) {
                return new Pair<String, String>(atribute.getText(), value.getText());
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
                HEADController.setDataLabel("These are all the current objects inside the " + document.getName() + " document that have " + result.get().getValue() + " as " + result.get().getKey());
                HEADController.loadTable(document, document.searchByAttribute(result.get().getValue(), result.get().getKey()));
            }
        } else {
            Alert emptySlots = new Alert(Alert.AlertType.WARNING);
            emptySlots.setTitle("WARNING");
            emptySlots.setHeaderText("Empty Slots");
            emptySlots.setContentText("At least the value field must be filled");
            emptySlots.showAndWait();
            searchObject(document);
        }
    }

    @FXML
    public void changeDirectory(ActionEvent event) {
        Alert directoryName = new Alert(Alert.AlertType.CONFIRMATION);
        directoryName.setTitle("Directory");
        directoryName.setHeaderText("Directory's path");
        directoryName.setContentText("All data is being saved in: \n" + ListUI.getMainPath() +
                "\n\n Do you want to set a new directory?");

        Optional<ButtonType> result = directoryName.showAndWait();
        if (result.get() == ButtonType.OK) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("New Directory");
            dialog.setHeaderText("Directory's path");
            dialog.setContentText("Please enter the new path:");

            Optional<String> resultInput = dialog.showAndWait();
            if (resultInput.isPresent()) {
                ListUI.changePath(resultInput.get());
            }
        }
    }

    public void loadTree() {
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(false);
        if (!ListUI.getListUI().isEmpty()) {
            makeStoreBranch(ListUI.getListUI(), root);
        }
        tree.setRoot(root);
        tree.setShowRoot(false);
        tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new ContextMenuNode();
            }
        });
    }

    public void loadTable(JSONDocument document, ObservableList<JSONInstance> elements) {
        Node<String> columns = document.getAttributesNames().getHead();
        tableView.getColumns().clear();
        tableView.getItems().clear();
        if (elements.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("No Matching Results");
            alert.setContentText("There aren't any objects with that atribute inside the " + document.getName() + " document");
            alert.showAndWait();
            return;
        }
        Integer numColumns = document.getAttributes().length();
        if (numColumns < 5) {
            tableView.setPrefWidth(558);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            if (elements.size() < 17) {
                tableView.setPrefHeight(428);
            } else {
                tableView.setPrefHeight(Control.USE_COMPUTED_SIZE);
            }
        } else {
            tableView.setPrefWidth(Control.USE_COMPUTED_SIZE);
            if (elements.size() < 17) {
                tableView.setPrefHeight(415);
            } else {
                tableView.setPrefHeight(Control.USE_COMPUTED_SIZE);
            }
        }
        columns = document.getAttributesNames().getHead();
        while (columns != null) {
            String attribute = columns.getValue();
            TableColumn<JSONInstance, String> column = new TableColumn<>(attribute);
            column.setMinWidth(100);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().findValue(attribute)));
            tableView.getColumns().add(column);
            columns = columns.getNext();
        }
        tableView.getItems().addAll(elements);
    }

    public void setDataLabel(String data) {
        showDataLabel.setText(data);
    }

    public void storeDocumentDialog(String action) throws IOException {
        JSONStore store = null;
        JSONDocument document = null;
        Dialog<Pair<String, String>> storeAndDocument = new Dialog<>();
        storeAndDocument.setTitle("Search");
        storeAndDocument.setHeaderText("Search by Atribute.\n\nEnter the name of the store and,\nthe document in which you want to search");
        ButtonType searchButton1 = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        storeAndDocument.getDialogPane().getButtonTypes().addAll(searchButton1, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField storeInput = new TextField();
        storeInput.setPromptText("Store's Name");
        TextField documentInput = new TextField();
        documentInput.setPromptText("Document's Value");
        grid.add(new Label("Store:"), 0, 1);
        grid.add(storeInput, 1, 1);
        grid.add(new Label("Document:"), 0, 2);
        grid.add(documentInput, 1, 2);

        storeAndDocument.getDialogPane().setContent(grid);
        storeAndDocument.setResultConverter(dialogButton -> {
            if (dialogButton == searchButton1) {
                return new Pair<>(storeInput.getText(), documentInput.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> resultStoreAndDocument = storeAndDocument.showAndWait();

        if (!resultStoreAndDocument.isPresent()) {
            return;
        }

        if (!resultStoreAndDocument.get().getValue().equals("") && !resultStoreAndDocument.get().getKey().equals("")) {
            store = ListUI.findStore(resultStoreAndDocument.get().getKey());
            document = ListUI.findDocument(resultStoreAndDocument.get().getValue(), store);
            if (store == null) {
                Alert invalidPath = new Alert(Alert.AlertType.ERROR);
                invalidPath.setTitle("ERROR");
                invalidPath.setHeaderText("Invalid store");
                invalidPath.setContentText("There's no store with that name");
                invalidPath.showAndWait();
                storeDocumentDialog(action);
                return;
            } else if (document == null) {
                Alert invalidPath = new Alert(Alert.AlertType.ERROR);
                invalidPath.setTitle("ERROR");
                invalidPath.setHeaderText("Invalid document");
                invalidPath.setContentText("There's no document with that name");
                invalidPath.showAndWait();
                storeDocumentDialog(action);
                return;
            } else {
                if (action.equalsIgnoreCase("Search")) {
                    searchObject(document);
                } else if (action.equalsIgnoreCase("CreateInstance")) {
                    createInstance(document, store);
                }
            }

        } else {
            Alert emptySlots = new Alert(Alert.AlertType.WARNING);
            emptySlots.setTitle("WARNING");
            emptySlots.setHeaderText("Empty Slots");
            emptySlots.setContentText("All slots must be filled");
            emptySlots.showAndWait();
            storeDocumentDialog(action);
            return;
        }
    }

    public void selectedImage(MouseEvent event) {
        ActionEvent realEvent = new ActionEvent();
        String source = event.getPickResult().getIntersectedNode().getId();
        try {
            if (source.equals(addStorePNG.getId())) {
                createStore(realEvent);
            } else if (source.equals(addDocumentPNG.getId())) {
                createDocument(realEvent);
            } else if (source.equals(addAttributePNG.getId())) {
                createAttribute(realEvent);
            } else if (source.equals(commitPNG.getId())) {
                ListUI.write();
            } else if (source.equals(searchPNG.getId())) {
                storeDocumentDialog("Search");
            } else if (source.equals(newInstancePNG.getId()))
                storeDocumentDialog("createInstance");
        } catch (IOException e) {
        }
    }

    public void makeStoreBranch(List<JSONStore> list, TreeItem<String> parent) {
        Node<JSONStore> current = list.getHead();
        do {
            TreeStoreNode item = new TreeStoreNode(current.getValue().getName());
            makeDocumentBranch(current.getValue().getDocuments(), item);
            item.setExpanded(true);
            parent.getChildren().add(item);
            current = current.getNext();

        } while (current != list.getHead());
    }

    public void makeDocumentBranch(List<JSONDocument> list, TreeItem<String> parent) {
        Node<JSONDocument> current = list.getHead();
        while (current != null) {
            TreeDocumentNode item = new TreeDocumentNode(current.getValue(), current.getValue().getStore());
            makeInstanceBranch(current.getValue().getInstances(), item);
            item.setExpanded(true);
            parent.getChildren().add(item);
            current = current.getNext();
        }
    }

    public void makeInstanceBranch(List<JSONInstance> list, TreeItem<String> parent) {
        Node<JSONInstance> current = list.getHead();
        parent.setExpanded(true);
        while (current != null) {
            TreeInstanceNode item = new TreeInstanceNode(current.getValue().getStore(), current.getValue().getDoc(), current.getValue().getName());
            item.setExpanded(true);
            parent.getChildren().add(item);
            current = current.getNext();
        }
    }



}
