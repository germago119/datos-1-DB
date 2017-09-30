package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONStore;
import LinkedDB.ListMain.ListUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * This class defines the Node for the Instances on the TreeView.
 *
 * @author Roger Valderrama
 */

public class TreeInstanceNode extends TreeAbstractNode {

    private JSONStore store;
    private JSONDocument document;
    private String instance;


    TreeInstanceNode(String storeName, String documentName, String instanceName) {
        this.setValue(instanceName);
        instance = instanceName;
        store = ListUI.findStore(storeName);
        document = ListUI.findDocument(documentName, store);
    }


    @Override
    public ContextMenu getMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                document.deletePrimaryKey(instance);
                HEADController.loadTree();
            }
        });
        menu.getItems().add(delete);
        return menu;
    }

}
