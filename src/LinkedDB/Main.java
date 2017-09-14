package LinkedDB;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent start = FXMLLoader.load(getClass().getResource("Start.fxml"));
        primaryStage.setTitle("LinkedDB");
        primaryStage.setScene(new Scene(start, 950, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


// todo close request
// todo switch scenes
// todo alertbox
// todo usar lambdas "->"
// todo snackbar for finish saving
// todo error dialogs in controller
// todo treeview
// todo use gson





