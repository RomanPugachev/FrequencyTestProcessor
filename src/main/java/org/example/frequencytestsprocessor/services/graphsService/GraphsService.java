package org.example.frequencytestsprocessor.services.graphsService;

import javafx.beans.binding.Bindings;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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

import java.util.*;
import java.util.stream.Collectors;


// TODO: implement Bode diagram visualization
public class GraphsService {
    private MainController mainController;
    private LineChart lineChart;
    private Map<String, FRF> FRFsForVisualization = new HashMap<>();
    private Map<String, FRF> pinnedFRFs = new HashMap<>();
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private List<Paint> colorPreset;

    public GraphsService(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateDataSets(Map<String, FRF> FRFsForVisualization) {
    }

    public void initializeService() {
//        lineChart = mainController.getGraphsLineChart();
        if (true) return;
        lineChart.setOnMouseMoved(event -> handleMouseMoved(event));
        colorPreset = new ArrayList<>();
        colorPreset.add(Paint.valueOf("red"));
        colorPreset.add(Paint.valueOf("blue"));
        colorPreset.add(Paint.valueOf("green"));
        colorPreset.add(Paint.valueOf("yellow"));
        colorPreset.add(Paint.valueOf("orange"));

//        lineChart.widthProperty().addListener((observable, oldValue, newValue) -> redrawLineChart());
//        lineChart.heightProperty().addListener((observable, oldValue, newValue) -> redrawLineChart());
        updateDataSets(new HashMap<>());
    }

    public void clearCharts(){
        lineChart.getData().clear();
    }


    private void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
    }
}
