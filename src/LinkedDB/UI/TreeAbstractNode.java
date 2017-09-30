package LinkedDB.UI;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

/**
 * This class defines the Abstract class for the new TreeView and its ContextMenu that shows on the Main Window
 *
 * @author Roger Valderrama
 */

public abstract class TreeAbstractNode extends TreeItem<String> {

    public abstract ContextMenu getMenu();
}
