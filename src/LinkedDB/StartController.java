package LinkedDB;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable{
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    Parent root = FXMLLoader.load(getClass().getResource("LinkedDB.fxml"));


}


