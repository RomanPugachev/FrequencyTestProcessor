package org.example.frequencytestsprocessor.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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

import static org.example.frequencytestsprocessor.commons.CommonMethods.*;
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
    private TextField insertingFRFNameTextField;

    @FXML
    private Rectangle selectionTimeLimitsRectangle;

    @FXML
    private Rectangle selectionTransformedDatasetRectangle;

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

    @Setter
    private TimeDialogCommitHandler timedialogCommitHandler;

    private LanguageNotifier languageNotifier;

    TimeSeriesDataSource chosenTimeSeriesDataSource;

    private Complex[] transformedData;

    public void initializeServices(String currentLanguage, TimeSeriesDataSource chosenTimeSeriesDataSource) {
        this.chosenTimeSeriesDataSource = chosenTimeSeriesDataSource;
        initializeTextFields();
        initializeLineCharts();
        initializeChoiceBox(chosenTimeSeriesDataSource);
        initializeLanguageService(currentLanguage);
        addSinusTimeSeriesInSource();
        setupWidgetsBehaviour();
        redrawDatasetChart();
    }

    private void initializeLineCharts() {
        xAxisTime.setAutoRanging(false);
        yAxisTime.setAutoRanging(false);
        xAxisTransformed.setAutoRanging(false);
        yAxisTransformed.setAutoRanging(false);

        timeDatasetChart.legendVisibleProperty().set(false);
        transformedDatasetChart.legendVisibleProperty().set(false);

        timeDatasetChart.setCreateSymbols(false);
        transformedDatasetChart.setCreateSymbols(false);

        makeChartZoomable(transformedDatasetChart);

        enableChooseLimitsOfTimeSeries();
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
        assert insertingFRFNameTextField != null : "fx:id=\"insertingFRFNameTextField\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert selectionTimeLimitsRectangle != null : "fx:id=\"selectionTimeLimitsRectangle\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
        assert selectionTransformedDatasetRectangle != null : "fx:id=\"selectionTransformedDatasetRectangle\" was not injected: check your FXML file 'time_data_source_dialog.fxml'.";
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

        int count = 0;

        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        while (timeStampsIterator.hasNext() && timeDataIterator.hasNext()) {
            double x = timeStampsIterator.next();
            double y = timeDataIterator.next();
            if (x >= leftBorder && x <= rightBorder) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                count++;
            } else if (x > rightBorder) {
                break;
            }
        }

        int sampleRate = Math.max(count / 1000, 1);
        count = 0;
        timeStampsIterator = chosenTimeSeriesDataSource.getTimeStamps1().iterator();
        timeDataIterator = datasetChoiseBox.getValue().getTimeData().iterator();
        while (timeStampsIterator.hasNext() && timeDataIterator.hasNext()) {
            double x = timeStampsIterator.next();
            double y = timeDataIterator.next();
            if (x >= leftBorder && x <= rightBorder) {
                if (count % sampleRate == 0) {
                    dataPoints.add(new XYChart.Data<>(x, y));
                }
                count++;
            } else if (x > rightBorder) {
                break;
            }
        }
        timeSeries.getData().addAll(dataPoints);
        zoomToArea(timeDatasetChart, leftBorder, rightBorder, minY - (maxY - minY) * 0.05, maxY + (maxY - minY) * 0.05);
        timeDatasetChart.getData().add(timeSeries);
        updateTransformedData();
    }

    private void updateTransformedData() {
        List<Double> dataToTransform = getDataForFourierTransforms(datasetChoiseBox.getValue().getTimeData(), chosenTimeSeriesDataSource.getTimeStamps1(), Double.valueOf(leftBorderTextField.getText()), Double.valueOf(rightBorderTextField.getText()));
        setTransformedData(FourierTransforms.fft(dataToTransform));
    }

    private void setTransformedData(Complex[] transformedData) {
        this.transformedData = transformedData;
        updateFourierTransformChart();
    }

    private void updateFourierTransformChart() {

        // Clear existing data from the chart and extracting timeStamps limit and number of points
        transformedDatasetChart.getData().clear();
        XYChart.Series<Number, Number> transformedSeries = new XYChart.Series<>();
        transformedSeries.setName("Transformed dataset");
        Double leftBorder = Double.valueOf(leftBorderTextField.getText()), rightBorder = Double.valueOf(rightBorderTextField.getText());
        double transformTimeRange = getTimeRangeOfBorderedSeries(chosenTimeSeriesDataSource.getTimeStamps1(), leftBorder, rightBorder);

        int numberOfTimeStamps = 0;
        Iterator<Double> sourceTimeStampsIterator = chosenTimeSeriesDataSource.getTimeStamps1().iterator();
        while (sourceTimeStampsIterator.hasNext()) {
            double timeStamp = sourceTimeStampsIterator.next();
            if (timeStamp >= leftBorder && timeStamp <= rightBorder) {
                numberOfTimeStamps++;
            } else if (timeStamp > rightBorder) {
                break;
            }
        }

        int sampleRate = Math.max(numberOfTimeStamps / 1000, 1);
        int graphPointsNumber = (int) Math.ceil(transformedData.length / sampleRate);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Creating DataPoints for the chart with sample rate and setting limits
        List<XYChart.Data<Number, Number>> dataPoints = new ArrayList<>(graphPointsNumber);
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        double minX = 0, maxX = (transformedData.length - 1) / transformTimeRange;
        for (int i = 0; i < transformedData.length / sampleRate; i++) {
            double frequency = (i * sampleRate) / transformTimeRange;
            double y = Complex.getModuleAsDouble(transformedData[i * sampleRate]);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            dataPoints.add(new XYChart.Data<>(frequency, y));
        }
        transformedSeries.getData().addAll(dataPoints);
        zoomToArea(transformedDatasetChart, minX, maxX, minY - (maxY - minY) * 0.05, maxY + (maxY - minY) * 0.05);
        transformedDatasetChart.getData().add(transformedSeries);
    }

    private void addSinusTimeSeriesInSource() {
        TimeSeriesDataset sinusTimeSeries = new TimeSeriesDataset();
        List<Double> values = new LinkedList<>();

        double sinusFrequency = 3;
        double sinusAmplitude = 1;
        for (double timeStampFromSource : chosenTimeSeriesDataSource.getTimeStamps1()) {
            values.add(sinusAmplitude * Math.sin(2 * Math.PI * sinusFrequency * timeStampFromSource));
        }
        sinusTimeSeries.setTimeData(values);
        sinusTimeSeries.setDatasetName("Test sinus time series " + sinusFrequency + " Hz");
        chosenTimeSeriesDataSource.addTimeSeriesDataset(sinusTimeSeries);
        datasetChoiseBox.getItems().add(sinusTimeSeries);
    }

    @FunctionalInterface
    public interface TimeDialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit(TimeSeriesDataset parentTimeSeriesDataset, Double leftLimit, Double rightLimit, String name);
    }
    @FXML
    private void invokeHandlingConfirmation(){
        TimeSeriesDataset parentTimeSeriesDataset = datasetChoiseBox.getValue();
        Double leftBorder = Double.valueOf(leftBorderTextField.getText());
        Double rightBorder = Double.valueOf(rightBorderTextField.getText());
        String name = insertingFRFNameTextField.getText();
        String errorMessage = "";
        if (parentTimeSeriesDataset == null) {
            errorMessage += "Time series dataset is not chosen";
        }
        if (leftBorder >= rightBorder) {
            errorMessage += "Right border value must be bigger then value in left border\n";
        }
        if (name == null || name.isBlank()) {
            errorMessage += "Fill text field with name of FRF\n";
        }
        if (errorMessage.isBlank()){
            timedialogCommitHandler.handleCommit(parentTimeSeriesDataset, leftBorder, rightBorder, name);
        } else {
            showAlert("Error", "Couldn't create FRF by chosen parameters:", errorMessage);
        }
    }
    @FXML
    private void invokeHandlingCancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void makeChartZoomable(LineChart<Number, Number> chart) {
        List<Double> dragStartValues = new ArrayList<>(Arrays.asList(0.0, 0.0));
        selectionTransformedDatasetRectangle.setManaged(false);

        Boolean[] isZoomed = new Boolean[]{false};

        chart.setOnMousePressed(event -> {
            dragStartValues.set(0, event.getX());
            dragStartValues.set(1, event.getY());

            selectionTransformedDatasetRectangle.setX(dragStartValues.get(0));
            selectionTransformedDatasetRectangle.setY(dragStartValues.get(1));
            selectionTransformedDatasetRectangle.setWidth(0);
            selectionTransformedDatasetRectangle.setHeight(0);
            selectionTransformedDatasetRectangle.setVisible(true);
        });

        chart.setOnMouseDragged(event -> {
            double dragEndX = event.getX();
            double dragEndY = event.getY();

            selectionTransformedDatasetRectangle.setX(Math.min(dragStartValues.get(0), dragEndX));
            selectionTransformedDatasetRectangle.setY(Math.min(dragStartValues.get(1), dragEndY));
            selectionTransformedDatasetRectangle.setWidth(Math.abs(dragEndX - dragStartValues.get(0)));
            selectionTransformedDatasetRectangle.setHeight(Math.abs(dragEndY - dragStartValues.get(1)));
        });

        chart.setOnMouseReleased(event -> {
            selectionTransformedDatasetRectangle.setVisible(false);
            double dragEndX = event.getX();
            double dragEndY = event.getY();

            if (Math.abs(dragStartValues.get(0) - dragEndX) > 5 && Math.abs(dragStartValues.get(1) - dragEndY) > 5) {
                // Convert screen coords to axis values using plot area only
                double xChartStart = chart.getXAxis().sceneToLocal(chart.localToScene(dragStartValues.get(0), 0)).getX();
                double xChartEnd = chart.getXAxis().sceneToLocal(chart.localToScene(dragEndX, 0)).getX();
                double yChartStart = chart.getYAxis().sceneToLocal(chart.localToScene(0, dragStartValues.get(1))).getY();
                double yChartEnd = chart.getYAxis().sceneToLocal(chart.localToScene(0, dragEndY)).getY();

                double xLowerZoom = ((NumberAxis) chart.getXAxis()).getValueForDisplay(Math.min(xChartStart, xChartEnd)).doubleValue();
                double xUpperZoom = ((NumberAxis) chart.getXAxis()).getValueForDisplay(Math.max(xChartStart, xChartEnd)).doubleValue();
                double yLowerZoom = ((NumberAxis) chart.getYAxis()).getValueForDisplay(Math.max(yChartStart, yChartEnd)).doubleValue(); // flipped because Y increases downward
                double yUpperZoom = ((NumberAxis) chart.getYAxis()).getValueForDisplay(Math.min(yChartStart, yChartEnd)).doubleValue();

                zoomToArea(chart, xLowerZoom, xUpperZoom, yLowerZoom, yUpperZoom);
                isZoomed[0] = true;
            }
        });

        chart.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && isZoomed[0]) {
                resetZoom(chart);
                isZoomed[0] = false;
            }
        });

    }

    private void resetZoom(LineChart<Number, Number> chart) {
        ObservableList<XYChart.Data<Number, Number>> transformedData = transformedDatasetChart.getData().getFirst().getData();
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        double minX = 0, maxX = Double.MIN_VALUE;
        for (XYChart.Data<Number, Number> dataPoint : transformedData) {
            double x = dataPoint.getXValue().doubleValue();
            double y = dataPoint.getYValue().doubleValue();
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        ((NumberAxis) chart.getXAxis()).setLowerBound(minX );
        ((NumberAxis) chart.getXAxis()).setUpperBound(maxX);
        ((NumberAxis) chart.getYAxis()).setLowerBound(minY - (maxY - minY) * 0.05);
        ((NumberAxis) chart.getYAxis()).setUpperBound(maxY + (maxY - minY) * 0.05);
    }

    private void enableChooseLimitsOfTimeSeries(){
        List<Double> dragStartValues = new ArrayList<>(Arrays.asList(0.0, 0.0));
        selectionTimeLimitsRectangle.setManaged(false);

        timeDatasetChart.setOnMousePressed(event -> {
            dragStartValues.set(0, event.getX());
            dragStartValues.set(1, event.getY());

            selectionTimeLimitsRectangle.setX(dragStartValues.get(0));
            selectionTimeLimitsRectangle.setY(dragStartValues.get(1));
            selectionTimeLimitsRectangle.setWidth(0);
            selectionTimeLimitsRectangle.setHeight(0);
            selectionTimeLimitsRectangle.setVisible(true);
        });

        timeDatasetChart.setOnMouseDragged(event -> {
            double dragEndX = event.getX();
            double dragEndY = event.getY();

            selectionTimeLimitsRectangle.setX(Math.min(dragStartValues.get(0), dragEndX));
            selectionTimeLimitsRectangle.setY(Math.min(dragStartValues.get(1), dragEndY));
            selectionTimeLimitsRectangle.setWidth(Math.abs(dragEndX - dragStartValues.get(0)));
            selectionTimeLimitsRectangle.setHeight(Math.abs(dragEndY - dragStartValues.get(1)));
        });

        timeDatasetChart.setOnMouseReleased(event -> {
            selectionTimeLimitsRectangle.setVisible(false);
            double dragEndX = event.getX();
            double dragEndY = event.getY();

            if (Math.abs(dragStartValues.get(0) - dragEndX) > 5 && Math.abs(dragStartValues.get(1) - dragEndY) > 5) {
                // Convert screen coords to axis values using plot area only
                double xChartStart = timeDatasetChart.getXAxis().sceneToLocal(timeDatasetChart.localToScene(dragStartValues.get(0), 0)).getX();
                double xChartEnd = timeDatasetChart.getXAxis().sceneToLocal(timeDatasetChart.localToScene(dragEndX, 0)).getX();
                double yChartStart = timeDatasetChart.getYAxis().sceneToLocal(timeDatasetChart.localToScene(0, dragStartValues.get(1))).getY();
                double yChartEnd = timeDatasetChart.getYAxis().sceneToLocal(timeDatasetChart.localToScene(0, dragEndY)).getY();

                double xLowerZoom = ((NumberAxis) timeDatasetChart.getXAxis()).getValueForDisplay(Math.min(xChartStart, xChartEnd)).doubleValue();
                double xUpperZoom = ((NumberAxis) timeDatasetChart.getXAxis()).getValueForDisplay(Math.max(xChartStart, xChartEnd)).doubleValue();
                double yLowerZoom = ((NumberAxis) timeDatasetChart.getYAxis()).getValueForDisplay(Math.max(yChartStart, yChartEnd)).doubleValue(); // flipped because Y increases downward
                double yUpperZoom = ((NumberAxis) timeDatasetChart.getYAxis()).getValueForDisplay(Math.min(yChartStart, yChartEnd)).doubleValue();

                leftBorderTextField.setText(xLowerZoom + "");
                rightBorderTextField.setText(xUpperZoom + "");
                redrawDatasetChart();
            }
        });

        timeDatasetChart.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                resetTimeLimits();
            }
        });
    }

    private void resetTimeLimits() {
        leftBorderTextField.setText(chosenTimeSeriesDataSource.getTimeStamps1().getFirst() + "");
        rightBorderTextField.setText(chosenTimeSeriesDataSource.getTimeStamps1().getLast() + "");
        redrawDatasetChart();
    }
}
