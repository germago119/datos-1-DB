package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONDocument;
import LinkedDB.JSONFILES.JSONInstance;
import javafx.collections.ObservableList;

/**
 * This class defines the HEADController that facilitates the connection on the main Window and its different controllers.
 *
 * @author Roger Valderrama
 */

public class HEADController {

    private static Controller controller;

    public static void loadTree() {
        controller.loadTree();
    }

    public static void loadTable(JSONDocument document, ObservableList<JSONInstance> elements) {
        controller.loadTable(document, elements);
    }

    public static void setDataLabel(String data) {
        controller.setDataLabel(data);
    }

    public Controller getController() {
        return controller;
    }

    public static void setController(Controller controllerA) {
        controller = controllerA;
    }


}
