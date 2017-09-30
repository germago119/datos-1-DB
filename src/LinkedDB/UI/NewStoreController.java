package LinkedDB.UI;

import LinkedDB.ListMain.ListUI;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class defines the controller for the new Store Window that creates new Stores
 *
 * @author Roger Valderrama
 */

public class NewStoreController {

    @FXML
    private JFXButton createStoreButton;

    @FXML
    private JFXTextField storeName;


    @FXML
    void createStore(ActionEvent event) {
        String storeName = this.storeName.getText();
        Stage stage = (Stage) createStoreButton.getScene().getWindow();
        if (storeName.trim().isEmpty()) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Empty slots");
            invalidName.setContentText("Please fill in all mandatory fields");
            invalidName.showAndWait();
        } else if (ListUI.findStore(storeName) != null) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Already existing store Name");
            invalidName.setContentText("Please enter a diferent name for your store");
            invalidName.showAndWait();
        } else {
            ListUI.addStore(storeName);
            stage.close();
            HEADController.loadTree();
        }
    }


}
