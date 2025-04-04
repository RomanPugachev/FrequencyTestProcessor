package org.example.frequencytestsprocessor.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import javafx.util.Duration;
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
        timeDatasetChart.getXAxis().setAutoRanging(false);
        timeDatasetChart.getYAxis().setAutoRanging(false);

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
            timeSeries.setName("Current time series dataset");
            double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
            double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
            for (int i = 0; i < newValue.getTimeData().size(); i++) {
                minX = Math.min(minX, chosenTimeSeriesDataSource.getTimeStamps1().get(i));
                maxX = Math.max(maxX, chosenTimeSeriesDataSource.getTimeStamps1().get(i));
                minY = Math.min(minY, newValue.getTimeData().get(i));
                maxY = Math.max(maxY, newValue.getTimeData().get(i));
                timeSeries.getData().add(new XYChart.Data<>(chosenTimeSeriesDataSource.getTimeStamps1().get(i), newValue.getTimeData().get(i)));
            }
            timeDatasetChart.setLegendVisible(false);
//            ((NumberAxis) timeDatasetChart.getXAxis()).setLowerBound(-100000);
//            ((NumberAxis) timeDatasetChart.getYAxis()).setLowerBound(-100000);
//            ((NumberAxis) timeDatasetChart.getXAxis()).setUpperBound(100000);
//            ((NumberAxis) timeDatasetChart.getYAxis()).setUpperBound(100000);
            ((NumberAxis) timeDatasetChart.getXAxis()).setLowerBound(minX - (maxX - minX) * 0.05);
            ((NumberAxis) timeDatasetChart.getYAxis()).setLowerBound(minY - (maxY - minY) * 0.05);
            ((NumberAxis) timeDatasetChart.getXAxis()).setUpperBound(maxX + (maxX - minX) * 0.05);
            ((NumberAxis) timeDatasetChart.getYAxis()).setUpperBound(maxY + (maxY - minY) * 0.05);
            timeDatasetChart.getData().add(timeSeries);
            applyDragAndDrop(timeSeries, (NumberAxis) timeDatasetChart.getXAxis(), (NumberAxis) timeDatasetChart.getYAxis());
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

    private void applyDragAndDrop(XYChart.Series<Number, Number> series, NumberAxis xAxis, NumberAxis yAxis) {
        for (XYChart.Data<Number, Number> data : series.getData()) {
            Node node = data.getNode();

            // Ensure the node exists.  Sometimes it takes a rendering cycle for it to be created.
            if (node != null) {
                makeDraggable(node, data, xAxis, yAxis);
            } else {
                // If node is null, defer the setup until it exists using a Platform.runLater() call.
                // This happens during initial setup if the chart renders before all data points
                javafx.application.Platform.runLater(() -> {
                    Node dataNode = data.getNode();  //Try to get the node again
                    if(dataNode != null) {
                        makeDraggable(dataNode, data, xAxis, yAxis);
                    } else {
                        System.err.println("Could not get data node for " + data); //Print Error if even after deferring, the node isn't found
                    }
                });
            }


            // Add a Tooltip to display the data values
            // Tooltip will now be handled inside makeDraggable
        }
    }

    private void makeDraggable(Node node, XYChart.Data<Number, Number> data, NumberAxis xAxis, NumberAxis yAxis) {
        final double[] dragDelta = new double[2];  // use an array to store deltas
        final double originalNodeSize = 6; //  or whatever the default size is
        final double hoverScaleFactor = 1.5; // How much to scale up on hover
        final Duration animationDuration = Duration.millis(100); // Animation speed

        // Use ScaleTransition for smooth size changes
        ScaleTransition hoverScaleUp = new ScaleTransition(animationDuration, node);
        hoverScaleUp.setFromX(1.0);
        hoverScaleUp.setFromY(1.0);
        hoverScaleUp.setToX(hoverScaleFactor);
        hoverScaleUp.setToY(hoverScaleFactor);


        ScaleTransition hoverScaleDown = new ScaleTransition(animationDuration, node);
        hoverScaleDown.setFromX(hoverScaleFactor);
        hoverScaleDown.setFromY(hoverScaleFactor);
        hoverScaleDown.setToX(1.0);
        hoverScaleDown.setToY(1.0);

        // Create and install the tooltip
        Tooltip tooltip = new Tooltip(String.format("(%.2f, %.2f)", data.getXValue().doubleValue(), data.getYValue().doubleValue()));
        Tooltip.install(node, tooltip);

        node.setOnMousePressed(event -> {
            timeDatasetChart.setAnimated(false);

            dragDelta[0] = node.getLayoutX() - event.getSceneX();
            dragDelta[1] = node.getLayoutY() - event.getSceneY();
            node.setCursor(javafx.scene.Cursor.MOVE);

            hoverScaleUp.stop();
            hoverScaleDown.stop();

            //  Ensure the node is at its normal size before starting a drag
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });

        node.setOnMouseReleased(event -> {
            node.setCursor(javafx.scene.Cursor.HAND);
            timeDatasetChart.setAnimated(true);
        });

        node.setOnMouseDragged(event -> {
            double newX = event.getSceneX() + dragDelta[0];
            double newY = event.getSceneY() + dragDelta[1];

            // Clamp the new X and Y positions to the chart's bounds.
            double xLowerBound = xAxis.getLowerBound();
            double xUpperBound = xAxis.getUpperBound();
            double yLowerBound = yAxis.getLowerBound();
            double yUpperBound = yAxis.getUpperBound();

            double xValue = xAxis.getValueForDisplay(newX).doubleValue();
            double yValue = yAxis.getValueForDisplay(newY).doubleValue();

            if (xValue >= xLowerBound && xValue <= xUpperBound &&
                    yValue >= yLowerBound && yValue <= yUpperBound) {

                data.setXValue(xValue);
                data.setYValue(yValue);

                //Force the chart to update, otherwise dragged location isn't correct.
                xAxis.requestAxisLayout();
                yAxis.requestAxisLayout();
            }
        });

        node.setOnMouseEntered(event -> {
            if (!event.isPrimaryButtonDown()) { // Only scale if not dragging
                node.setCursor(javafx.scene.Cursor.HAND);
                hoverScaleUp.play();
            }
        });

        node.setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) { // Only scale if not dragging
                node.setCursor(javafx.scene.Cursor.DEFAULT);
                hoverScaleDown.play();
            }
        });
    }


    @FunctionalInterface
    public interface TimeDialogCommitHandler {
        // Here will be the method that will be called when the user clicks the "Commit" button.
        void handleCommit(Collection<Long> chosenRuns, boolean showErrors);
    }

}
