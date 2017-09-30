package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * This class defines the controller for the main Window.
 *
 * @author Roger Valderrama
 */

public class DocumentDesignController {

    final String esp = "Especial";

    ObservableList<String> types = FXCollections.observableArrayList("Cadena", "Entero", "Flotante", "Fecha-hora", esp);

    @FXML
    private JFXCheckBox primaryKeyCheckBox;

    @FXML
    private JFXTextField storeName;

    @FXML
    private JFXTextField documentName;

    @FXML
    private JFXTextField attributeName;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private JFXRadioButton requiredCheck;

    @FXML
    private JFXTextField defaultValue;

    @FXML
    private JFXRadioButton foreignCheck;

    @FXML
    private JFXTextField foreignStore;

    @FXML
    private JFXTextField foreignDocument;

    @FXML
    private JFXButton createAttributeButton;


    private boolean emptySlots() {
        Boolean result = isEmpty(storeName) || isEmpty(documentName) || isEmpty(attributeName) ||
                typeChoiceBox.getValue() == null || (!defaultValue.isDisable() && isEmpty(defaultValue)) ||
                (!foreignDocument.isDisable() && (isEmpty(foreignDocument) || isEmpty(foreignStore)));
        return result;
    }


    public boolean isEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }

    @FXML
    private void initialize() {
        typeChoiceBox.setItems(types);
        typeChoiceBox.setValue("Cadena");
    }

    public void setStore(String name) {
        storeName.setText(name);
    }

    public void setDocument(String name) {
        documentName.setText(name);
    }

    public void enableDefaultValue() {
        Boolean state = !defaultValue.isDisable();
        defaultValue.setDisable(state);
    }

    public void enableForeignData() {
        if (!typeChoiceBox.getValue().equalsIgnoreCase(esp)) {
            Alert wrongType = new Alert(AlertType.WARNING);
            wrongType.setTitle("WARNING");
            wrongType.setHeaderText("Wrong Type Of Atribute");
            wrongType.setContentText("Atribute's  type must be \"Especial\" in order to add a foreign value to it");
            wrongType.showAndWait();
            foreignCheck.setSelected(false);
        }
        Boolean state = !foreignCheck.isSelected();
        foreignStore.setDisable(state);
        foreignDocument.setDisable(state);
    }

    @FXML
    public void createAttribute(ActionEvent event) {
        String storeName = this.storeName.getText();
        String docName = documentName.getText();
        Stage stage = (Stage) createAttributeButton.getScene().getWindow();
        JSONStore store = ListUI.findStore(storeName);
        JSONDocument document = ListUI.findDocument(docName, store);
        if (emptySlots()) {
            Alert emptySlots = new Alert(AlertType.WARNING);
            emptySlots.setTitle("WARNING");
            emptySlots.setHeaderText("Empty slots");
            emptySlots.setContentText("Please fill in all mandatory fields");
            emptySlots.showAndWait();
        } else if (store == null) {
            Alert unexistingStore = new Alert(AlertType.CONFIRMATION);
            unexistingStore.setTitle("WARNING");
            unexistingStore.setHeaderText("The store doesn't exist");
            unexistingStore.setContentText("Do yout want to create the store?");
            Optional<ButtonType> result = unexistingStore.showAndWait();
            if (result.get() == ButtonType.OK) {
                ListUI.addStore(storeName);
                createAttribute(event);
                return;
            }
        } else if (document == null) {
            Alert unexistingDocument = new Alert(AlertType.CONFIRMATION);
            unexistingDocument.setTitle("WARNING");
            unexistingDocument.setHeaderText("The document doesn't exist");
            unexistingDocument.setContentText("Do yout want to create the document?");
            Optional<ButtonType> result = unexistingDocument.showAndWait();
            if (result.get() == ButtonType.OK) {
                store.appendDocument(new JSONDocument(docName));
                HEADController.loadTree();
                createAttribute(event);
                return;
            }
        } else if (primaryKeyCheckBox.isSelected() && document.findPrimary() != null) {
            Alert alreadyKey = new Alert(AlertType.WARNING);
            alreadyKey.setTitle("WARNING");
            alreadyKey.setHeaderText("Existing Primary Key");
            alreadyKey.setContentText("This document already has a primary key set and, they can not have more than one primary key.");
            alreadyKey.showAndWait();


        } else if (foreignCheck.isSelected() && !typeChoiceBox.getValue().equalsIgnoreCase("Especial")) {
            Alert wrongType = new Alert(AlertType.WARNING);
            wrongType.setTitle("WARNING");
            wrongType.setHeaderText("Wrong Type Of Atribute");
            wrongType.setContentText("Atribute's  type must be \"Especial\" in order to add a foreign value to it");
            wrongType.showAndWait();

        } else if (foreignCheck.isSelected() && (ListUI.findStore(foreignStore.getText()) == null || ListUI.findDocument(foreignDocument.getText(),
                ListUI.findStore(foreignStore.getText())) == null)) {
            Alert invalidForeignStore = new Alert(AlertType.WARNING);
            invalidForeignStore.setTitle("WARNING");
            invalidForeignStore.setHeaderText("Invalid Store");

            if (ListUI.findStore(foreignStore.getText()) == null) {
                invalidForeignStore.setContentText("The foreign store does not exist");
            } else {
                invalidForeignStore.setContentText("The foreign document does not exist");
            }

            invalidForeignStore.showAndWait();
        } else if (ListUI.findDocument(docName, storeName).findAttribute(attributeName.getText()) != null) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Already existing atribute's name");
            invalidName.setContentText("Please enter a diferent name for your atribute");
            invalidName.showAndWait();
        } else if (attributeName.getText().equalsIgnoreCase("$REFERENCES$")) {
            Alert reserved = new Alert(AlertType.WARNING);
            reserved.setTitle("WARNING");
            reserved.setHeaderText("Reserved Value");
            reserved.setContentText("$REFERENCE$ is a reserved key, please change the atribute's name");
            reserved.showAndWait();
        } else {
            document.setAtributes(primaryKeyCheckBox.isSelected(), attributeName.getText(), typeChoiceBox.getValue(), defaultValue.isDisable(), defaultValue.getText(),
                    foreignStore.getText(), foreignDocument.getText());
            document.setStore(storeName);
            if (foreignCheck.isSelected()) {
                ListUI.findDocument(foreignDocument.getText(), foreignStore.getText()).addReference(storeName + "/" + docName);
            }
            HEADController.loadTree();
            stage.close();

        }
    }

}
