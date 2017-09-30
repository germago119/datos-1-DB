package LinkedDB.UI;

import javafx.scene.control.TreeCell;

/**
 * This class defines the implementation of a Node for the ContextMenu.
 *
 * @author Roger Valderrama
 */

public class ContextMenuNode extends TreeCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setGraphic(getTreeItem().getGraphic());
            TreeAbstractNode tempItem = (TreeAbstractNode) getTreeItem();
            setContextMenu(tempItem.getMenu());
        }
    }
}
