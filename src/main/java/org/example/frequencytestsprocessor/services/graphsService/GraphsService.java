// TODO: Debug visualization of datasets. Connect with selectors.

package org.example.frequencytestsprocessor.services.graphsService;

import javafx.beans.binding.Bindings;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

public class GraphsService {
    private MainController mainController;
    private Canvas canvas;
    private GraphicsContext gc;
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
        canvas = mainController.getGraphsCanvas();
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseMoved(event -> handleMouseMoved(event));
        colorPreset = new ArrayList<>();
        colorPreset.add(Paint.valueOf("red"));
        colorPreset.add(Paint.valueOf("blue"));
        colorPreset.add(Paint.valueOf("green"));
        colorPreset.add(Paint.valueOf("yellow"));
        colorPreset.add(Paint.valueOf("orange"));

        canvas.widthProperty().bind(mainController.getGraphsVBox().widthProperty());
        canvas.heightProperty().bind(Bindings.createDoubleBinding(() -> {
            return mainController.getGraphsVBox().getHeight() - mainController.getGraphToolBar().getHeight();
        }, mainController.getGraphsVBox().heightProperty(), mainController.getGraphToolBar().heightProperty()));

        canvas.widthProperty().addListener((observable, oldValue, newValue) -> redrawCanvas());
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> redrawCanvas());
        initializeAxes(-1, -1, 1, 1);
        updateDataSets(new HashMap<>(), true);
    }

    public void saveCanvasToFile(String absPath){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void clearGraphicsContext() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void clearGraphicsContext(boolean hasFurtherProcessing) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
        redrawCanvas();
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
        gc.setStroke(Paint.valueOf("gray"));
        gc.setLineWidth(0.5);
        double wdth = canvas.getWidth();
        double hght = canvas.getHeight();
        if (wdth == 0 || hght == 0) return;
        double stepX = wdth / 10;
        double stepY = hght / 10;
        for (double x = 0; x <= wdth; x += stepX) {
            gc.strokeLine(x, 0, x, hght);
        }
        for (double y = 9; y <= hght; y += stepY) {
            gc.strokeLine(0, y, wdth, y);
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
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

    private void redrawCanvas() {
        visualizeDataSets(true);
//        showOnlyTextInCenter("Choose some data sets to visualize");
    }

    private void initializeAxes(double minX, double minY, double maxX, double maxY) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        // Draw X-axis
        gc.beginPath();
        gc.moveTo(0, canvas.getHeight() / 2);
        gc.lineTo(canvas.getWidth(), canvas.getHeight() / 2);
        gc.stroke();
        gc.closePath();

        // Draw Y-axis
        gc.beginPath();
        gc.moveTo(canvas.getWidth() / 2, 0);
        gc.lineTo(canvas.getWidth() / 2, canvas.getHeight());
        gc.stroke();
        gc.closePath();

        // Add labels for X-axis
        double xStep = (maxX - minX) / 10;
        for (int i = 0; i <= 10; i++) {
            double x = minX + i * xStep;
            double xPos = (i / 10.0) * canvas.getWidth();
            gc.strokeText(String.format("%.2f", x), xPos, canvas.getHeight() / 2 + 20);
        }

        // Add labels for Y-axis
        double yStep = (maxY - minY) / 10;
        for (int i = 0; i <= 10; i++) {
            double y = minY + i * yStep;
            double yPos = canvas.getHeight() - (i / 10.0) * canvas.getHeight();
            gc.strokeText(String.format("%.2f", y), canvas.getWidth() / 2 + 5, yPos);
        }
    }

    private void plotData(List<Double> xData, List<Double> yData, Paint color, boolean connectPoints) {
        if (color == null) {
            color = colorPreset.get(new Random().nextInt(colorPreset.size()));
        }
        gc.beginPath();
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        double xRange = maxX - minX;
        double yRange = maxY - minY;

        if (xRange == 0.0) xRange = 1.0;  // Avoid division by zero or scaling issues when all x-values the same.
        if (yRange == 0.0) yRange = 1.0;  // Avoid division by zero or scaling issues when all y-values the same.

        for (int i = 0; i < xData.size(); i++) {
            double x = (xData.get(i) - minX) / xRange * canvasWidth;
            double y = canvasHeight - (yData.get(i) - minY) / yRange * canvasHeight;

            if (i == 0) {
                gc.moveTo(x, y);
            } else {
                gc.lineTo(x, y);
            }

            gc.fillOval(x - 3, y - 3, 6, 6); // Draw point
        }

        if(connectPoints) {
            gc.stroke();
        }
        gc.closePath();
    }

    private void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
    }

    private void showOnlyTextInCenter(String text) {
        clearGraphicsContext(true);
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, canvas.getWidth() / 2, canvas.getHeight() / 2);
    }
}
