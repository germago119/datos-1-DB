package LinkedDB.UI;

import LinkedDB.JSONFILES.Files;
import LinkedDB.ListMain.ListUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

/**
 * This class defines the launcher that starts the whole project.
 *
 * @author Roger Valderrama
 */

public class Launcher extends Application {

    public static void main(String[] args) {
        ListUI.setMainPath(Files.readPath());
        ListUI.setPath(Paths.get(Files.readPath(), "LinkedDB").toString());
        ListUI.read();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LinkedDB.FXML"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("LinkedDB");
        primaryStage.setResizable(false);
        primaryStage.show();
        Controller controllerUI = loader.getController();
        HEADController.setController(controllerUI);
        HEADController.setDataLabel("");
        HEADController.loadTree();
    }
}
