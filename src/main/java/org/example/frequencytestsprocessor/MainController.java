package org.example.frequencytestsprocessor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Label labelHello;

    @FXML
    private AnchorPane mainField;

    @FXML
    void onAct1(ActionEvent event) {

    }

    @FXML
    void onAct2(ActionEvent event) {
        System.out.printf("You made action on btn2 %s\n", event.toString());
    }

    @FXML
    void initialize() {
        assert btn1 != null : "fx:id=\"btn1\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert btn2 != null : "fx:id=\"btn2\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert labelHello != null : "fx:id=\"labelHello\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainField != null : "fx:id=\"mainField\" was not injected: check your FXML file 'mainScene-view.fxml'.";

    }

}
