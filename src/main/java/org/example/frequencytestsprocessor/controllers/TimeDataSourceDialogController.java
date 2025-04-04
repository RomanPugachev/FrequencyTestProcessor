package org.example.frequencytestsprocessor.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.showAlert;
import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;
import static org.example.frequencytestsprocessor.commons.StaticStrings.PATH_TO_LANGUAGES;

public class TimeDataSourceDialogController {
////Objects of interface//////////////

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private HBox confirmationHBox;

    @FXML
    private HBox controlHBox;

    @FXML
    private ChoiceBox<TimeSeriesDataset> datasetChoiseBox;

    @FXML
    private Label headerLabel;

    @FXML
    private Label insertingFRFNameLabel;

    @FXML
    private Tooltip insertingFRFNameLabelTooltip;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private VBox mainVbox;

    @FXML
    private TextField runsForCalculationTextField;

    @FXML
    private LineChart<Number, Number> timeDatasetChart;

    @FXML
    private LineChart<Number, Number> transformedDatasetChart;

    // Common parameters and objects
    @Setter
    private TimeDialogCommitHandler timedialogCommitHandler;

    private LanguageNotifier languageNotifier;

    TimeSeriesDataSource chosenTimeSeriesDataSource;

    public void initializeServices(String currentLanguage, TimeSeriesDataSource chosenTimeSeriesDataSource) {
        this.chosenTimeSeriesDataSource = chosenTimeSeriesDataSource;
        initializeLineChart();
        initializeChoiseBox(chosenTimeSeriesDataSource);
        initializeLanguageService(currentLanguage);
        setupWidgetsBehaviour();
    }

    private void initializeLineChart() {
        timeDatasetChart.getXAxis().setLabel("Time");
        timeDatasetChart.getYAxis().setLabel("Amplitude");
        timeDatasetChart.getXAxis().setAutoRanging(true);
        timeDatasetChart.getYAxis().setAutoRanging(true);
        timeDatasetChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        timeDatasetChart.legendVisibleProperty().set(false);
//        TODO: continue setting up of graphics visualization

    }

    private void initializeLanguageService(String currentLanguage) {
        languageNotifier = new LanguageNotifier(PATH_TO_LANGUAGES + "/timeDataSourceDialogLanguage.properties");
        languageNotifier.addObserver(
                List.of(
                        (languageProperties, languageToSet, previousLanguage) -> {
                            String key = headerLabel.getId() + DOT;
                            String text = languageProperties.getProperty(key + languageToSet);
                            if (text != null) {
                                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                                headerLabel.setText(String.format(decodedText, this.chosenTimeSeriesDataSource.getSourceAddress()));
                            } else {
                                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                            }
                        },
                        new LanguageObserverDecorator<>(insertingFRFNameLabel),
                        new LanguageObserverDecorator<>(cancelButton),
                        new LanguageObserverDecorator<>(confirmButton)
                )
        );
        languageNotifier.changeLanguage(currentLanguage);
    }

    private void initializeChoiseBox(TimeSeriesDataSource chosenTimeSeriesDataSource) {
        datasetChoiseBox.getItems().addAll(chosenTimeSeriesDataSource.getTimeSeriesDatasets());
        datasetChoiseBox.getSelectionModel().select(chosenTimeSeriesDataSource.getTimeSeriesDatasets().get(0));
    }

    @FXML
    void initialize() {
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert confirmButton != null : "fx:id=\"confirmButton\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert confirmationHBox != null : "fx:id=\"confirmationHBox\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert controlHBox != null : "fx:id=\"controlHBox\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert datasetChoiseBox != null : "fx:id=\"datasetChoiseBox\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert headerLabel != null : "fx:id=\"headerLabel\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert insertingFRFNameLabel != null : "fx:id=\"insertingFRFNameLabel\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert insertingFRFNameLabelTooltip != null : "fx:id=\"insertingFRFNameLabelTooltip\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert mainVbox != null : "fx:id=\"mainVbox\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert runsForCalculationTextField != null : "fx:id=\"runsForCalculationTextField\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert timeDatasetChart != null : "fx:id=\"timeDatasetChart\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert transformedDatasetChart != null : "fx:id=\"transformedDatasetChart\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
    }

    private void setupWidgetsBehaviour(){
        datasetChoiseBox.setConverter(new StringConverter<TimeSeriesDataset>() {
            @Override
            public String toString(TimeSeriesDataset object) {
                return object.getDatasetName();
            }

            @Override
            public TimeSeriesDataset fromString(String string) {
                return null;
            }
        });
        datasetChoiseBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            timeDatasetChart.getData().clear();
            XYChart.Series<Number, Number> timeSeries = new XYChart.Series<>();
            for (int i = 0; i < newValue.getTimeData().size(); i++) {
                timeSeries.getData().add(new XYChart.Data<>(chosenTimeSeriesDataSource.getTimeStamps1().get(i), newValue.getTimeData().get(i)));
            }
            timeDatasetChart.getData().add(timeSeries);
        });
//        cancelButton.setOnMouseClicked(event -> ((Stage) cancelButton.getScene().getWindow()).close());
//        confirmButton.setOnMouseClicked(event -> {
//            Map<String, String> incorrectItems = new HashMap<>();
//            Set<Long> chosenRuns = (Set<Long>) extractRuns(runsForCalculationTextField.getText(), incorrectItems);
//            if (!(ignoreWarningsCheckBox.isSelected()) && incorrectItems.size() >= 1) {
//                showAlert("Error", "Errors in extracting runs",
//                        incorrectItems.keySet().stream().map(k -> k + ":\n" + incorrectItems.get(k)).collect(Collectors.joining("\n\n"))); }
//            timedialogCommitHandler.handleCommit(chosenRuns, !(ignoreWarningsCheckBox.isSelected()));
//        });
    }


    @FunctionalInterface
    public interface TimeDialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit(Collection<Long> chosenRuns, boolean showErrors);
    }

}
