package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class defines the Node for the Stores on the TreeView.
 *
 * @author Roger Valderrama
 */

public class TreeStoreNode extends TreeAbstractNode {

    private JSONStore store;

    public TreeStoreNode(String name) {
        this.setValue(name);
        this.store = ListUI.findStore(name);
    }

    @Override
    public ContextMenu getMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem deleteStore = new MenuItem("Delete");
        deleteStore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListUI.getListUI().delete(store);
                HEADController.loadTree();
            }
        });
        MenuItem addDocument = new MenuItem("Add document");
        addDocument.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("StartDocument.fxml"));
                    Parent root = loader.load();
                    NewDocumentController controller = loader.getController();
                    controller.setStore(store.getName());
                    Stage stage = new Stage();
                    stage.setTitle("Create a new Store");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.showAndWait();

                } catch (IOException ex) {
                }
            }
        });

        menu.getItems().addAll(deleteStore, addDocument);
        return menu;
    }
}
