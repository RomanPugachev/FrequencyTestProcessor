package org.example.frequencytestsprocessor.controllers;

import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import org.example.frequencytestsprocessor.datamodel.myMath.FourierTransforms;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.ENTER;
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
    private Tooltip insertRunsForCalculationLabelTooltip;

    @FXML
    private Tooltip insertRunsForCalculationLabelTooltip1;

    @FXML
    private Tooltip insertRunsForCalculationLabelTooltip2;

    @FXML
    private Label insertingFRFNameLabel;

    @FXML
    private Label leftBorderLabel;

    @FXML
    private TextField leftBorderTextField;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private VBox mainVbox;

    @FXML
    private Label rightBorderLabel;

    @FXML
    private TextField rightBorderTextField;

    @FXML
    private TextField runsForCalculationTextField;

    @FXML
    private Rectangle selectionRectangle;

    @FXML
    private LineChart<Number, Number> timeDatasetChart;

    @FXML
    private LineChart<Number, Number> transformedDatasetChart;

    @FXML
    private NumberAxis xAxisTime;

    @FXML
    private NumberAxis yAxisTime;

    @FXML
    private NumberAxis xAxisTransformed;

    @FXML
    private NumberAxis yAxisTransformed;

    // Common parameters and objects
    // Variables for hovering implementation
    private double startX, startY; // Mouse press coordinates

    private boolean isHovering = false;
    private String defaultBorder;
    private String hoverBorder = "-fx-border-color: blue; -fx-border-width: 2;";

    // Store original axis ranges for unzooming
    private double originalXLowerBound, originalXUpperBound, originalYLowerBound, originalYUpperBound;
    private boolean isZoomed = false;

    @Setter
    private TimeDialogCommitHandler timedialogCommitHandler;

    private LanguageNotifier languageNotifier;

    TimeSeriesDataSource chosenTimeSeriesDataSource;

    public void initializeServices(String currentLanguage, TimeSeriesDataSource chosenTimeSeriesDataSource) {
        this.chosenTimeSeriesDataSource = chosenTimeSeriesDataSource;
        initializeTextFields();
        initializeLineCharts();
        initializeChoiceBox(chosenTimeSeriesDataSource);
        initializeLanguageService(currentLanguage);
        setupWidgetsBehaviour();
        redrawDatasetChart();
    }

    private void initializeLineCharts() {
        xAxisTime.setAutoRanging(false);
        yAxisTime.setAutoRanging(false);

        timeDatasetChart.legendVisibleProperty().set(false);

//        TODO: continue setting up of graphics visualization

    }

    private void initializeTextFields() {
        double leftBorder = chosenTimeSeriesDataSource.getTimeStamps1().get(0);
        double rightBorder = chosenTimeSeriesDataSource.getTimeStamps1().get(chosenTimeSeriesDataSource.getTimeStamps1().size() - 1);
        leftBorderTextField.setText(String.valueOf(leftBorder));
        rightBorderTextField.setText(String.valueOf(rightBorder));
    }

    private void initializeLanguageService(String currentLanguage) {
        languageNotifier = new LanguageNotifier(PATH_TO_LANGUAGES + "/timeDataSourceDialogLanguage.properties");
        languageNotifier.addObserver(
                List.of(
                        observeAxis(xAxisTime),
                        observeAxis(yAxisTime),
                        observeAxis(xAxisTransformed),
                        observeAxis(yAxisTransformed),
                        new LanguageObserverDecorator<>(leftBorderLabel),
                        new LanguageObserverDecorator<>(rightBorderLabel),
                        new LanguageObserverDecorator<>(insertingFRFNameLabel),
                        new LanguageObserverDecorator<>(cancelButton),
                        new LanguageObserverDecorator<>(confirmButton),
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
                        }
                )
        );
        languageNotifier.changeLanguage(currentLanguage);
    }

    private void initializeChoiceBox(TimeSeriesDataSource chosenTimeSeriesDataSource) {
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
        assert insertRunsForCalculationLabelTooltip != null : "fx:id=\"insertRunsForCalculationLabelTooltip\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert insertRunsForCalculationLabelTooltip1 != null : "fx:id=\"insertRunsForCalculationLabelTooltip1\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert insertRunsForCalculationLabelTooltip2 != null : "fx:id=\"insertRunsForCalculationLabelTooltip2\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert insertingFRFNameLabel != null : "fx:id=\"insertingFRFNameLabel\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert leftBorderLabel != null : "fx:id=\"leftBorderLabel\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert leftBorderTextField != null : "fx:id=\"leftBorderTextField\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert mainVbox != null : "fx:id=\"mainVbox\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert rightBorderLabel != null : "fx:id=\"rightBorderLabel\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert rightBorderTextField != null : "fx:id=\"rightBorderTextField\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert runsForCalculationTextField != null : "fx:id=\"runsForCalculationTextField\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert selectionRectangle != null : "fx:id=\"selectionRectangle\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert timeDatasetChart != null : "fx:id=\"timeDatasetChart\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert transformedDatasetChart != null : "fx:id=\"transformedDatasetChart\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert xAxisTime != null : "fx:id=\"xAxisTime\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert yAxisTime != null : "fx:id=\"yAxisTime\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
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
            redrawDatasetChart();
        });
        listenSubmitBorderUpdate(leftBorderTextField);
        listenSubmitBorderUpdate(rightBorderTextField);
    }

    private void listenSubmitBorderUpdate(TextField borderTextField) {
        borderTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    double value = Double.parseDouble(newValue);
                    // Update the left border value
                    // Add your logic here to handle the new left border value
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
                borderTextField.setText(oldValue);
            } finally {
                redrawDatasetChart();
            }
        });
    }

    private void redrawDatasetChart() {
        timeDatasetChart.getData().clear();
        XYChart.Series<Number, Number> timeSeries = new XYChart.Series<>();
        timeSeries.setName("Current time series dataset");

        double leftBorder = Double.parseDouble(leftBorderTextField.getText());
        double rightBorder = Double.parseDouble(rightBorderTextField.getText());

        Iterator<Double> timeStampsIterator = chosenTimeSeriesDataSource.getTimeStamps1().iterator();
        Iterator<Double> timeDataIterator = datasetChoiseBox.getValue().getTimeData().iterator();

        List<XYChart.Data<Number, Number>> dataPoints = new ArrayList<>(chosenTimeSeriesDataSource.getTimeStamps1().size());

        int count = 0, sampleRate = Math.max(chosenTimeSeriesDataSource.getTimeStamps1().size() / 1000, 1);

        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        while (timeStampsIterator.hasNext() && timeDataIterator.hasNext()) {
            double x = timeStampsIterator.next();
            double y = timeDataIterator.next();
            if (x >= leftBorder && x <= rightBorder) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                if (count % sampleRate == 0) {
                    dataPoints.add(new XYChart.Data<>(x, y));
                }
                count++;
            } else if (x > rightBorder) {
                break;
            }
        }
        timeSeries.getData().addAll(dataPoints);
        xAxisTime.setLowerBound(leftBorder);
        xAxisTime.setUpperBound(rightBorder);
        yAxisTime.setLowerBound(minY - (maxY - minY) * 0.05);
        yAxisTime.setUpperBound(maxY + (maxY - minY) * 0.05);
        timeDatasetChart.getData().add(timeSeries);
        updateFourierTransformChart();
    }

    private void updateFourierTransformChart() {
        transformedDatasetChart.getData().clear();
        XYChart.Series<Number, Number> transformedSeries = new XYChart.Series<>();
        transformedSeries.setName("Transformed dataset");

        Complex[] transformedData = FourierTransforms.fft(timeDatasetChart.getData().get(0).getData().stream().map(dataPoint -> (Double) dataPoint.getYValue()).toList());

        List<Double> frequencies = new LinkedList<>();
        List<Double> includedTimeStamps = new LinkedList<>();
        Iterator<Double> sourceTimeStampsIterator = chosenTimeSeriesDataSource.getTimeStamps1().iterator();
        while (sourceTimeStampsIterator.hasNext()) {
            double timeStamp = sourceTimeStampsIterator.next();
            if (timeStamp >= Double.valueOf(leftBorderTextField.getText()) && timeStamp <= Double.valueOf(rightBorderTextField.getText())) {
                includedTimeStamps.add(timeStamp);
            }
        }
        for (int i = 0; i < transformedData.length; i++) {
            double frequency = i / (includedTimeStamps.size() * (includedTimeStamps.get(1) - includedTimeStamps.get(0)));
            frequencies.add(frequency);
            transformedSeries.getData().add(new XYChart.Data<>(frequency, Complex.getModuleAsDouble(transformedData[i])));
        }
        transformedDatasetChart.getData().add(transformedSeries);

    }

    private static LanguageObserver observeAxis(NumberAxis axis) {
        return (languageProperties, languageToSet, previousLanguage) -> {
            String key = axis.getId() + DOT;
            String text = languageProperties.getProperty(key + languageToSet);
            if (text != null) {
                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                axis.setLabel(decodedText);
            } else {
                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
            }
        };
    }

    @FunctionalInterface
    public interface TimeDialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit(Collection<Long> chosenRuns, boolean showErrors);
    }

}
