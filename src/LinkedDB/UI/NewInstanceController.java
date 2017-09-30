package LinkedDB.UI;

import LinkedDB.JSONFILES.JSONInstance;
import LinkedDB.JSONFILES.Metadata;
import LinkedDB.ListMain.ListUI;
import LinkedDB.Lists.List;
import LinkedDB.Lists.Node;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Field;

/**
 * This class defines the controller for the new Instance Window that creates new Instances
 *
 * @author Roger Valderrama
 */

public class NewInstanceController {

    private String document;
    private String store;

    final String message = " is not instance of ";

    @FXML
    private VBox vBoxTextField;

    @FXML
    private VBox vBoxLabels;

    @FXML
    private VBox vBoxTypes;

    @FXML
    private VBox vBoxRequired;

    @FXML
    private VBox vBoxSpecial;


    @FXML
    private Label documentLabel;

    @FXML
    private JFXButton createInstanceButton;

    private static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createInstance(ActionEvent event) {
        Stage stage = (Stage) createInstanceButton.getScene().getWindow();
        JSONInstance instance = new JSONInstance("Instancia");
        instance.setDocument(document);
        instance.setStore(store);

        ObservableList<javafx.scene.Node> atributes = vBoxLabels.getChildren();
        ObservableList<javafx.scene.Node> values = vBoxTextField.getChildren();
        ObservableList<javafx.scene.Node> special = vBoxSpecial.getChildren();
        ObservableList<javafx.scene.Node> types = vBoxTypes.getChildren();

        String incorrectType = incorrectType(values, types);

        if (isEmpty(values)) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Empty slots");
            invalidName.setContentText("Please fill in all mandatory fields");
            invalidName.showAndWait();
        } else if (incorrectType != null) {
            Alert invalidName = new Alert(AlertType.WARNING);
            invalidName.setTitle("WARNING");
            invalidName.setHeaderText("Incorrect Type");
            invalidName.setContentText(incorrectType);
            invalidName.showAndWait();
        } else {
            for (int i = 0; i < atributes.size(); i++) {
                Label atributeLabel = (Label) atributes.get(i);
                TextField valueField = (TextField) values.get(i);
                Label specialLabel = (Label) special.get(i);
                if (specialLabel.getText().equals("Primary")) {
                    instance.setName(valueField.getText().toString());
                } else if (specialLabel.getText().equals("Foreign")) {
                    String[] foreignData = specialLabel.getTooltip().getText().split("/");
                    JSONInstance currentInstance = ListUI.findInstance(foreignData[0], foreignData[1], valueField.getText().toString());
                    if (currentInstance == null) {
                        Alert invalidForeignInput = new Alert(AlertType.WARNING);
                        invalidForeignInput.setTitle("WARNING");
                        invalidForeignInput.setHeaderText("Incorrect Type");
                        invalidForeignInput.setContentText("The foreign object referenced as " + atributeLabel.getText() +
                                " doesn't exist");
                        invalidForeignInput.showAndWait();
                        return;
                    } else {
                        currentInstance.addReference(instance.getStore() + "/" + instance.getDoc() + "/" + instance.getName());
                    }
                }
                instance.addValue(atributeLabel.getText(), valueField.getText());
            }
            ListUI.addInstance(ListUI.findDocument(document, store), instance);
            stage.close();
            HEADController.loadTree();
        }
    }

    private String incorrectType(ObservableList<javafx.scene.Node> values, ObservableList<javafx.scene.Node> types) {
        for (int i = 0; i < values.size(); i++) {
            Label tempType = (Label) types.get(i);
            TextField tempField = (TextField) values.get(i);
            String value = tempField.getText();
            if (tempType.getText().equalsIgnoreCase("Entero")) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException exception) {
                    return value + message + tempType.getText();
                }
            } else if (tempType.getText().equalsIgnoreCase("Flotante")) {
                try {
                    Float.parseFloat(value);
                } catch (NumberFormatException exception) {
                    return value + message + tempType.getText();
                }
            } else if (tempType.getText().equalsIgnoreCase("Fecha-hora")) {
            } else if (tempType.getText().equalsIgnoreCase("Especial")) {
            } else {
                try {
                    Integer.parseInt(value);
                    return value + message + tempType.getText();
                } catch (NumberFormatException exception) {
                }
                try {
                    Float.parseFloat(value);
                    return value + message + tempType.getText();
                } catch (NumberFormatException exception) {
                }
            }

        }
        return null;
    }

    public boolean isEmpty(ObservableList<javafx.scene.Node> list) {
        for (javafx.scene.Node aList : list) {
            TextField tempField = (TextField) aList;
            if (tempField.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    void initialize(List<Metadata> list) {
        Node<Metadata> currentNode = list.getHead();

        while (currentNode != null) {
            Metadata currentData = currentNode.getValue();
            vBoxLabels.getChildren().add(new Label(currentData.getData()));
            vBoxRequired.getChildren().add(new Label(currentData.getRequired().toString().toUpperCase()));

            if (!currentData.getRequired()) {
                vBoxTextField.getChildren().add(new TextField(currentData.getDefaultValue()));
            } else {
                vBoxTextField.getChildren().add(new TextField());
            }

            vBoxTypes.getChildren().add(new Label(currentData.getType()));

            if (currentData.getPrimary()) {
                vBoxSpecial.getChildren().add(new Label("Primary"));
            } else if (!currentData.getForeignStore().equals("")) {
                Label label = new Label("Foreign");
                Tooltip tooltip = new Tooltip(currentData.getForeignStore() + "/" + currentData.getForeignDocument());
                hackTooltipStartTiming(tooltip);
                label.setTooltip(tooltip);
                vBoxSpecial.getChildren().add(label);
            } else {
                vBoxSpecial.getChildren().add(new Label("Normal"));
            }
            currentNode = currentNode.getNext();
        }
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setStore(String store) {
        this.store = store;
    }

    void setDocument() {
        documentLabel.setText(this.document + " Document, inside the " + this.store + " Store");
    }

}
