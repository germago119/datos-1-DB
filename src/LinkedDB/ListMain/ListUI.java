package LinkedDB.ListMain;

import LinkedDB.JSONFILES.*;
import LinkedDB.Lists.List;
import LinkedDB.Lists.ListCircular;
import LinkedDB.Lists.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListUI {

    private static ListCircular<JSONStore> listUI;
    private static String path;
    private static String mainPath;

    private ListUI() {

    }

    public static List<JSONStore> getListUI() {
        if (listUI == null) {
            listUI = new ListCircular<>("ListUI");
        }
        return listUI;
    }

    @Nullable
    public static JSONStore findStore(String Name) {
        getListUI();
        Node<JSONStore> current = listUI.getHead();
        JSONStore result = null;
        if (current == null) {
            return null;
        }
        do {
            if (current.getValue().getName().equals(Name)) {
                result = current.getValue();
                break;
            }
            current = current.getNext();
        } while (current != listUI.getHead());
        return result;
    }

    public static void addStore(String Name) {
        getListUI();
        listUI.append(new JSONStore(Name));
    }

    public static void addInstance(JSONDocument document, JSONInstance instance) {
        document.addInstance(instance);
    }

    @Contract("_, null -> null")
    public static JSONDocument findDocument(String doc, JSONStore store) {
        if (store == null) {
            return null;
        }
        return store.findDocument(doc);
    }

    public static JSONDocument findDocument(String doc, String store) {
        return findDocument(doc, findStore(store));
    }

    public static void write() {
        getListUI();
        try {
            DocumentWriter.write(listUI, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        getListUI();
        DocumentReader.read(listUI, path);
    }

    public static void deleteInstances(JSONDocument doc) {
        doc.getInstances().deleteAll();
    }

    public static void changePath(String nextPath) {
        try {
            Path location = Paths.get(nextPath);
            if (location.toFile().exists()) {
                if (getMainPath() != null) {
                    Files.emptyFolder(getPath(), false);
                }
                path = Files.generateFolder(nextPath, "LinkedDB");
                setMainPath(nextPath);
                Files.writePath(nextPath);
                write();
            } else {
                Alert invalidPath = new Alert(AlertType.ERROR);
                invalidPath.setTitle("ERROR");
                invalidPath.setHeaderText("Invalid Path Name");
                invalidPath.setContentText("The directory doesn't exist");
                invalidPath.showAndWait();
            }
        } catch (InvalidPathException exception) {
            Alert invalidPath = new Alert(AlertType.ERROR);
            invalidPath.setTitle("ERROR");
            invalidPath.setHeaderText("Invalid Path Name");
            invalidPath.setContentText("The directory's name has invalid characters");
            invalidPath.showAndWait();
        }

    }

    @Contract(pure = true)
    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        ListUI.path = path;
    }

    @Contract(pure = true)
    public static String getMainPath() {
        return mainPath;
    }

    public static void setMainPath(String path) {
        mainPath = path;
    }

    public static void deleteStore(JSONStore store) {
        listUI.delete(store);
    }

    public static void deleteDocument(JSONDocument doc, JSONStore store) {
        store.getDocuments().delete(doc);
    }

    @Nullable
    public static JSONInstance findInstance(String store, String doc, String instance) {
        JSONDocument document = findDocument(doc, store);
        if (document == null) {
            return null;
        }
        return document.findInstance(instance);

    }
}
