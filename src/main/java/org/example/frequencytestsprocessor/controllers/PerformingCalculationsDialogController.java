package org.example.frequencytestsprocessor.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class PerformingCalculationsDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    void initialize() {
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";

    }

}
