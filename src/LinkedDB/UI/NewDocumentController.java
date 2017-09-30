package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * This class defines the controller for the new Document Window that creates new Documents
 *
 * @author Roger Valderrama
 */

public class NewDocumentController {

    @FXML
    private JFXButton createDocumentButton;

    @FXML
    private JFXTextField storeName;

    @FXML
    private JFXTextField documentName;


    @FXML
    void createDocument(ActionEvent event) {

        String doc = documentName.getText();
        String storeName = this.storeName.getText();
        JSONStore store = ListUI.findStore(storeName);
        Stage stage = (Stage) createDocumentButton.getScene().getWindow();

        if (storeName.trim().isEmpty() || doc.trim().isEmpty()) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Empty slots");
            invalidName.setContentText("Please fill in all mandatory fields");
            invalidName.showAndWait();
        } else if (store == null) {
            Alert unexistingStore = new Alert(AlertType.CONFIRMATION);
            unexistingStore.setTitle("WARNING");
            unexistingStore.setHeaderText("The store doesn't exist");
            unexistingStore.setContentText("Do yout want to create the store?");
            Optional<ButtonType> result = unexistingStore.showAndWait();
            if (result.get() == ButtonType.OK) {
                ListUI.addStore(storeName);
                createDocument(event);
            }
        } else if (ListUI.findDocument(doc, store) != null) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Already existing document Name");
            invalidName.setContentText("Please enter a diferent name for your document");
            invalidName.showAndWait();
        } else {
            JSONDocument document = new JSONDocument(doc);
            document.setStore(storeName);
            store.appendDocument(document);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StartDocumentDesign.fxml"));
                Parent root = loader.load();
                DocumentDesignController controller = loader.getController();
                controller.setStore(storeName);
                controller.setDocument(document.getName());
                Stage stage2 = new Stage();
                stage2.setTitle("Create a new Attribute");
                stage2.setScene(new Scene(root));
                stage2.setResizable(false);
                stage2.showAndWait();

            } catch (IOException exception) {
                exception.printStackTrace();
            }

            HEADController.loadTree();
            stage.close();
        }
    }

    public void setStore(String value) {
        storeName.setText(value);
        documentName.setFocusTraversable(true);
    }

}
