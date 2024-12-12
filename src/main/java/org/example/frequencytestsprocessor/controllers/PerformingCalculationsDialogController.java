package org.example.frequencytestsprocessor.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;

public class PerformingCalculationsDialogController {
////Objects of interface//////////////

    @FXML
    private Text availableRunsText;

    @FXML
    private TextFlow availableRunsTextFlow;

    @FXML
    private Label headerLabel;

    @FXML
    private HBox headerLabelHBox;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private VBox mainVbox;

    @FXML
    private HBox textFlowHbox;

    // Common parameters
    @Setter
    private DialogCommitHandler dialogCommitHandler;

    @FXML
    void initialize() {

        String longText = "This is a very long text that goes on for quite a while.  It's meant to demonstrate how to control how much of it is shown initially, enabling the user to show more or less of the text using a button. This is a great example of how to implement a show more/show less control.";
        assert availableRunsText != null : "fx:id=\"availableRunsText\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert availableRunsTextFlow != null : "fx:id=\"availableRunsTextFlow\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert headerLabel != null : "fx:id=\"headerLabel\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert headerLabelHBox != null : "fx:id=\"headerLabelHBox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainVbox != null : "fx:id=\"mainVbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert textFlowHbox != null : "fx:id=\"textFlowHbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
    }


    @FunctionalInterface
    public interface DialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit();
    }

}
