package org.example.frequencytestsprocessor.controllers;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;

import static org.example.frequencytestsprocessor.commons.CommonMethods.showAlert;
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
    private Tooltip ignoreWarningsCheckBoxTooltip;

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

    // Common parameters and objects
    @Setter
    private DialogCommitHandler dialogCommitHandler;

    private LanguageNotifier languageNotifier;

    List<? extends Number> sharedRuns;

    public void initializeServices(String currentLanguage, List<? extends Number> sharedRuns) {
        initializeLanguageService(currentLanguage);
        this.sharedRuns = sharedRuns;
        availableRunsText.setText(availableRunsText.getText() + this.sharedRuns.stream().sorted().map(String::valueOf).collect(Collectors.joining(", ")));

        setupWidgetsBehaviour();
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
        assert ignoreWarningsCheckBoxTooltip != null : "fx:id=\"ignoreWarningsCheckBoxTooltip\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert insertRunsForCalculationLabel != null : "fx:id=\"insertRunsForCalculationLabel\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert insertRunsForCalculationLabelTooltip != null : "fx:id=\"insertRunsForCalculationLabelTooltip\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert mainVbox != null : "fx:id=\"mainVbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert runsForCalculationTextField != null : "fx:id=\"runsForCalculationTextField\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
        assert textFlowHbox != null : "fx:id=\"textFlowHbox\" was not injected: check your FXML file 'performing_calculations_dialog.fxml'.";
    }

    private void setupWidgetsBehaviour(){
        cancelButton.setOnMouseClicked(event -> ((Stage) cancelButton.getScene().getWindow()).close());
        confirmButton.setOnMouseClicked(event -> {
            Map<String, String> incorrectItems = new HashMap<>();
            Set<Long> chosenRuns = (Set<Long>) extractRuns(runsForCalculationTextField.getText(), incorrectItems);
            if (!(ignoreWarningsCheckBox.isSelected()) || incorrectItems.size() < 1) { // TODO: don't call error notificaiton without errors
                showAlert("Error", "Errors in extracting runs",
                        incorrectItems.keySet().stream().map(k -> k + ":\n" + incorrectItems.get(k)).collect(Collectors.joining("\n\n"))); }
            dialogCommitHandler.handleCommit(chosenRuns, !(ignoreWarningsCheckBox.isSelected()));
        });
    }

    private Collection<Long> extractRuns(String sourceText, Map<String, String> incorrectItems) {
        Set<Long> extractedRuns = new HashSet<>();
        Arrays.stream(sourceText.replaceAll(" ", "").split(","))
                .forEach(item -> extractFromItem(item, incorrectItems, extractedRuns));
        return extractedRuns;
    }

    private void extractFromItem(String item, Map<String,String> incorrectItems, Set<Long> extractedRuns) {
        String[] tmp = item.split("-");
        if (tmp.length == 1) {
            try {
                Long firstLimit = Long.valueOf(tmp[0]);
                if (sharedRuns.contains(firstLimit)) {
                    extractedRuns.add(firstLimit);
                }
                else {
                    incorrectItems.put(item, "Run, which you add in calculations must be included in list of shared." +
                            firstLimit++ + "is not included in it.");
                }
            } catch (Exception e) { incorrectItems.put(item, "Insert only digit symbols as value for Run number"); }
        } else if (tmp.length == 2) {
            try {
                Long firstLimit = Long.valueOf(tmp[0]), secondLimit = Long.valueOf(tmp[1]);
                if (secondLimit < firstLimit) { incorrectItems.put(item, "Higher limit of Run entry can't be less then lower limit"); return; }
                while (firstLimit < secondLimit) {
                    if (sharedRuns.contains(firstLimit)) {
                        extractedRuns.add(firstLimit++);
                    }
                    else {
                        incorrectItems.put(item, "Run, which you add in calculations must be included in list of shared." +
                                firstLimit++ + "is not included in it.");
                    }
                }
            } catch (Exception e) { incorrectItems.put(item, "Insert only digit symbols as value for Run number"); }
        } else{
            incorrectItems.put(item, "Possibly, you added extra \"-\" sign in runs");
        }
    }

    @FunctionalInterface
    public interface DialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit(Collection<Long> chosenRuns, boolean showErrors);
    }

}
