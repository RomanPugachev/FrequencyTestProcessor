package org.example.frequencytestsprocessor.controllers;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;

import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

public class PerformingCalculationsDialogController {
////Objects of interface//////////////

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text availableRunsText;

    @FXML
    private TextFlow availableRunsTextFlow;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private HBox confirmationHBox;

    @FXML
    private HBox controlHBox;

    @FXML
    private CheckBox ignoreWarningsCheckBox;

    @FXML
    private Label insertRunsForCalculationLabel;

    @FXML
    private Tooltip insertRunsForCalculationLabelTooltip;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private VBox mainVbox;

    @FXML
    private TextField runsForCalculationTextField;

    @FXML
    private HBox textFlowHbox;

    // Common parameters
    @Setter
    private DialogCommitHandler dialogCommitHandler;

    // Common application parameters
    private LanguageNotifier languageNotifier;

    public void initializeServices(String currentLanguage) {
        initializeLanguageService(currentLanguage);
    }
    private void initializeLanguageService(String currentLanguage) {
        languageNotifier = new LanguageNotifier(PATH_TO_LANGUAGES + "/calculationsFileDialogLanguage.properties");
        languageNotifier.addObserver(
                List.of(
                        (languageProperties, languageToSet) -> {
                            String key = availableRunsText.getId() + DOT;
                            String text = languageProperties.getProperty(key + languageToSet);
                            if (text != null) {
                                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                                availableRunsText.setText(decodedText);
                            } else {
                                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                            }
                        },
                        (languageProperties, languageToSet) -> {
                            String key = insertRunsForCalculationLabelTooltip.getId() + DOT;
                            String text = languageProperties.getProperty(key + languageToSet);
                            if (text != null) {
                                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                                insertRunsForCalculationLabelTooltip.setText(decodedText);
                            } else {
                                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                            }
                        },
                        new LanguageObserverDecorator<>(insertRunsForCalculationLabel),
                        new LanguageObserverDecorator<>(ignoreWarningsCheckBox),
                        new LanguageObserverDecorator<>(cancelButton),
                        new LanguageObserverDecorator<>(confirmButton)
                )
        );
        languageNotifier.changeLanguage(currentLanguage);
    }
    @FXML
    void initialize() {
        String longText = "This is a very long text that goes on for quite a while.  It's meant to demonstrate how to control how much of it is shown initially, enabling the user to show more or less of the text using a button. This is a great example of how to implement a show more/show less control.";
        assert availableRunsText != null : "fx:id=\"availableRunsText\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert availableRunsTextFlow != null : "fx:id=\"availableRunsTextFlow\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert confirmButton != null : "fx:id=\"confirmButton\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert confirmationHBox != null : "fx:id=\"confirmationHBox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert controlHBox != null : "fx:id=\"controlHBox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert ignoreWarningsCheckBox != null : "fx:id=\"ignoreWarningsCheckBox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert insertRunsForCalculationLabel != null : "fx:id=\"insertRunsForCalculationLabel\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert insertRunsForCalculationLabelTooltip != null : "fx:id=\"insertRunsForCalculationLabelTooltip\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainVbox != null : "fx:id=\"mainVbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert runsForCalculationTextField != null : "fx:id=\"runsForCalculationTextField\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert textFlowHbox != null : "fx:id=\"textFlowHbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
    }


    @FunctionalInterface
    public interface DialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit();
    }

}
