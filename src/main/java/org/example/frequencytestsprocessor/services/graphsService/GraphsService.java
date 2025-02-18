package org.example.frequencytestsprocessor.services.graphsService;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
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
    }

    private void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
    }
}
