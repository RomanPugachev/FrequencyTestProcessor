package org.example.frequencytestsprocessor.services.graphsService;

import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.Canvas2DPrintable;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.*;
import java.util.stream.Collectors;


// TODO: Implement unpinning of graph
public class GraphsService {
    // TODO: implement showing tooltip on hovering -> https://habr.com/ru/articles/242009/
    private MainController mainController;
    private LineChart lineChart;
    private Map<String, FRF> FRFsForVisualization = new HashMap<>();
    @Getter
    private Map<String, FRF> pinnedFRFs = new HashMap<>();
    private List<Paint> colorPreset;
    private boolean drawNyquistLimitation;
    // Charts from main controller
    private LineChart<Number, Number> graphsLineChartNyquist;

    private LineChart<Number, Number> graphsLineChartBodeAmplitude;

    private LineChart<Number, Number> graphsLineChartBodePhase;


    public GraphsService(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateDataSets(Map<String, FRF> FRFsForVisualization) {
        this.FRFsForVisualization = FRFsForVisualization;
        redrawGraphs();
    }

    public void pinCurrentGraph(Map<String, FRF> FRFsForVisualization) {
        pinnedFRFs.putAll(FRFsForVisualization);
        redrawGraphs();
    }

    public void initializeService() {
        graphsLineChartBodeAmplitude = mainController.getGraphsLineChartBodeAmplitude();
        graphsLineChartBodeAmplitude.setTitle("Amplidude frequency response");
        graphsLineChartBodeAmplitude.getXAxis().setLabel("Frequency, Hz");
        graphsLineChartBodeAmplitude.getYAxis().setLabel("Amlitude");
        graphsLineChartBodePhase = mainController.getGraphsLineChartBodePhase();
        graphsLineChartBodePhase.setTitle("Phase frequency response");
        graphsLineChartBodePhase.getXAxis().setLabel("Frequency, Hz");
        graphsLineChartBodePhase.getYAxis().setLabel("Phase");
        graphsLineChartNyquist = mainController.getGraphsLineChartNyquist();
        graphsLineChartNyquist.setTitle("Nyquist diagram");
        graphsLineChartNyquist.getXAxis().setLabel("Real");
        graphsLineChartNyquist.getYAxis().setLabel("Imaginary");
        graphsLineChartNyquist.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        drawNyquistLimitation = false;
        colorPreset = new ArrayList<>();
        colorPreset.add(Paint.valueOf("red"));
        colorPreset.add(Paint.valueOf("blue"));
        colorPreset.add(Paint.valueOf("green"));
        colorPreset.add(Paint.valueOf("yellow"));
        colorPreset.add(Paint.valueOf("orange"));
        updateDataSets(new HashMap<>());
    }

    public void clearCharts(){
        graphsLineChartBodeAmplitude.getData().clear();
        graphsLineChartBodePhase.getData().clear();
        graphsLineChartNyquist.getData().clear();
    }
    private void redrawGraphs() {
        graphsLineChartBodeAmplitude.getData().clear();
        graphsLineChartBodePhase.getData().clear();
        graphsLineChartNyquist.getData().clear();
        FRFsForVisualization.forEach((idAndRunOfFRF, frf) -> {
            XYChart.Series<Number, Number> seriesBodeAmplitude = new XYChart.Series<>();
            seriesBodeAmplitude.setName("Dataset " + idAndRunOfFRF);
            XYChart.Series<Number, Number> seriesBodePhase = new XYChart.Series<>();
            seriesBodePhase.setName("Dataset " + idAndRunOfFRF);
            XYChart.Series<Number, Number> seriesNyquist = new XYChart.Series<>();
            seriesNyquist.setName("Dataset " + idAndRunOfFRF);

            addFRFSeries(frf, seriesBodeAmplitude, seriesBodePhase, seriesNyquist);
        });
        pinnedFRFs.forEach((idAndRunOfFRF, frf) -> {
            XYChart.Series<Number, Number> seriesBodeAmplitude = new XYChart.Series<>();
            seriesBodeAmplitude.setName("Pinned dataset " + idAndRunOfFRF);
            XYChart.Series<Number, Number> seriesBodePhase = new XYChart.Series<>();
            seriesBodePhase.setName("Pinned dataset " + idAndRunOfFRF);
            XYChart.Series<Number, Number> seriesNyquist = new XYChart.Series<>();
            seriesNyquist.setName("Pinned dataset " + idAndRunOfFRF);

            addFRFSeries(frf, seriesBodeAmplitude, seriesBodePhase, seriesNyquist);
        });
    }

    private void addFRFSeries(FRF frf, XYChart.Series<Number, Number> seriesBodeAmplitude, XYChart.Series<Number, Number> seriesBodePhase, XYChart.Series<Number, Number> seriesNyquist) {
        List<Double> currentFrequencies = frf.getFrequencies();
        List<Complex> currentValues = frf.getComplexValues();

        double currentReal, currentImag;

        for (int i =0; i<currentFrequencies.size(); i++) {
            currentReal = currentValues.get(i).getReal();
            currentImag = currentValues.get(i).getImag();
            seriesBodeAmplitude.getData().add(new XYChart.Data<>(
                    currentFrequencies.get(i),
                    Complex.getModuleAsDouble(currentValues.get(i)))
            );
            seriesBodePhase.getData().add(new XYChart.Data<>(
                    currentFrequencies.get(i),
                    Complex.getAngle(currentValues.get(i)))
            );
            seriesNyquist.getData().add(new XYChart.Data<>(
                    currentReal,
                    currentImag)
            );
        }

        graphsLineChartBodeAmplitude.getData().add(seriesBodeAmplitude);
        graphsLineChartBodePhase.getData().add(seriesBodePhase);
        graphsLineChartNyquist.getData().add(seriesNyquist);

        // TODO: add drag and drop
        applyDragAndDrop(seriesBodeAmplitude, (NumberAxis) graphsLineChartBodeAmplitude.getXAxis(), (NumberAxis) graphsLineChartBodeAmplitude.getYAxis());
        applyDragAndDrop(seriesBodePhase, (NumberAxis) graphsLineChartBodePhase.getXAxis(), (NumberAxis) graphsLineChartBodePhase.getYAxis());
        applyDragAndDrop(seriesNyquist, (NumberAxis) graphsLineChartNyquist.getXAxis(), (NumberAxis) graphsLineChartNyquist.getYAxis());
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
            graphsLineChartBodeAmplitude.setAnimated(false);
            graphsLineChartBodePhase.setAnimated(false);
            graphsLineChartNyquist.setAnimated(false);

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
            graphsLineChartBodeAmplitude.setAnimated(true);
            graphsLineChartBodePhase.setAnimated(true);
            graphsLineChartNyquist.setAnimated(true);

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
}
