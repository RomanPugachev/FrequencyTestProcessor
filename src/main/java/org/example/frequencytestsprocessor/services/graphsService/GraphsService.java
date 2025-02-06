package org.example.frequencytestsprocessor.services.graphsService;

import javafx.beans.binding.Bindings;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.Canvas2DPrintable;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;

import java.util.*;
import java.util.stream.Collectors;


// TODO: implement Bode diagram visualization
public class GraphsService {
    private MainController mainController;
    private LineChart lineChart;
    private Map<String, Canvas2DPrintable> canvas2DPrintableDataSets = new HashMap<>();
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private final double additionalPersentage = 0.1/2;
    private List<Paint> colorPreset;
    private boolean showGrid=true;

    public GraphsService(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateDataSets(Map<String, Canvas2DPrintable> canvas2DPrintableDataSets, boolean connectPoints) {
        this.setCanvas2DPrintableDataSets(canvas2DPrintableDataSets);
        visualizeDataSets(connectPoints);
    }

    public void initializeService() {
        lineChart = mainController.getGraphsLineChart();
        lineChart.setOnMouseMoved(event -> handleMouseMoved(event));
        colorPreset = new ArrayList<>();
        colorPreset.add(Paint.valueOf("red"));
        colorPreset.add(Paint.valueOf("blue"));
        colorPreset.add(Paint.valueOf("green"));
        colorPreset.add(Paint.valueOf("yellow"));
        colorPreset.add(Paint.valueOf("orange"));

        lineChart.widthProperty().addListener((observable, oldValue, newValue) -> redrawLineChart());
        lineChart.heightProperty().addListener((observable, oldValue, newValue) -> redrawLineChart());
        initializeAxes(-1, -1, 1, 1);
        updateDataSets(new HashMap<>(), true);
    }

    public void saveLineChartToFile(String absPath){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void clearGraphicsContext() {
        throw new UnsupportedOperationException("Now this function is not implemented");
    }

    public void clearGraphicsContext(boolean hasFurtherProcessing) {
        gc.clearRect(0, 0, lineChart.getWidth(), lineChart.getHeight());
        if (!hasFurtherProcessing) {
            showOnlyTextInCenter("Choose some data sets to visualize");
        }
    }

    private void setCanvas2DPrintableDataSets(Map<String, Canvas2DPrintable> canvas2DPrintableDataSets) {
        if (canvas2DPrintableDataSets.isEmpty()) {
            minX = -1;
            maxX = 1;
            minY = -1;
            maxY = 1;
            this.canvas2DPrintableDataSets = canvas2DPrintableDataSets;
            return;
        }
        this.canvas2DPrintableDataSets = canvas2DPrintableDataSets;
        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;
        this.canvas2DPrintableDataSets.values().forEach(canvas2DPrintable -> {
            List<Double> xData = canvas2DPrintable.getXData();
            List<Double> yData = canvas2DPrintable.getYData();
            xData.forEach(x -> {
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            });
            yData.forEach(y -> {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            });
        });
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        if (deltaX > 0) {
            maxX += deltaX * additionalPersentage;
            minX -= deltaX * additionalPersentage;
        } else {
            maxX += 0.00001;
            minX -= 0.00001;
        }
        if (deltaY > 0) {
            maxY += deltaY * additionalPersentage;
            minY -= deltaY * additionalPersentage;
        } else {
            maxY += 0.00001;
            minY -= 0.00001;
        }
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        redrawLineChart();
    }

    private void visualizeDataSets(boolean connectPoints) {
        clearGraphicsContext(true);
        initializeAxes(minX, minY, maxX, maxY);
        if (showGrid) drawGrid();
        int paintNumber = 0;
        canvas2DPrintableDataSets.values()
                .forEach((canvas2DPrintableDataSet) -> {
                    this.plotData(canvas2DPrintableDataSet.getXData(), canvas2DPrintableDataSet.getYData(), colorPreset.get(paintNumber % colorPreset.size()), connectPoints);
                });
    }

    private void drawGrid() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void generateExample(int numberOfPoints, double linearCoefficient, boolean connectPoints){
        List<Double> xData = new ArrayList<>(numberOfPoints);
        List<Double> yData = new ArrayList<>(numberOfPoints);
        for (int i=0; i<numberOfPoints;i++){
            xData.add(i * 1.0);
            yData.add(linearCoefficient * xData.get(i));
        }
        HashMap<String, Canvas2DPrintable> canvas2DPrintableDataSets = new HashMap<>();
        canvas2DPrintableDataSets.put("Example1", new RepresentableDataset(xData, yData) {
            @Override
            public List<Double> getXData() {
                return xData;
            }
            @Override
            public List<Double> getYData() {
                return yData;
            }
        });
        canvas2DPrintableDataSets.put("Example2", new RepresentableDataset(xData, yData) {
            @Override
            public List<Double> getXData() {
                return xData.stream().map(x -> x  - 0.3).collect(Collectors.toList());
            }
            @Override
            public List<Double> getYData() {
                return yData.stream().map(y -> y * 2 + 0.2).collect(Collectors.toList());
            }
        });
        updateDataSets(canvas2DPrintableDataSets, connectPoints);
    }

    private void redrawLineChart() {
        visualizeDataSets(true);
    }

    private void initializeAxes(double minX, double minY, double maxX, double maxY) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void plotData(List<Double> xData, List<Double> yData, Paint color, boolean connectPoints) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
    }

}
