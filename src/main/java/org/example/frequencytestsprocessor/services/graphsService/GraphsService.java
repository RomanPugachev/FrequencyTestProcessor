package org.example.frequencytestsprocessor.services.graphsService;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.frequencytestsprocessor.controllers.MainController;

import java.util.*;

public class GraphsService {
    private MainController mainController;
    private Canvas canvas;
    private GraphicsContext gc;
    private List<Map.Entry<List<Double>, List<Double>>> data;
    private List<Paint> colorPreset;

    public GraphsService(MainController mainController) {
        this.mainController = mainController;
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
    }

    public void plotData(List<Double> xData, List<Double> yData, Paint color) {
        if (color == null) {
            color = colorPreset.get(new Random().nextInt(colorPreset.size()));
        }
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.beginPath();
        for (int i = 0; i < xData.size(); i++) {
            gc.lineTo(xData.get(i), yData.get(i));
        }
        gc.stroke();
    }

    public void generateExample(int numberOfPoints, int linearCoefficient, boolean connectPoints){
        List<Double> xData = new ArrayList<>(numberOfPoints);
        List<Double> yData = new ArrayList<>(numberOfPoints);
        for (int i=0; i<numberOfPoints;i++){
            xData.add(i * 1.0);
            yData.add(linearCoefficient * xData.get(i));
        }
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear previous drawings
        gc.beginPath();
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        double minX = xData.stream().min(Double::compare).orElse(0.0);
        double maxX = xData.stream().max(Double::compare).orElse(1.0);
        double minY = yData.stream().min(Double::compare).orElse(0.0);
        double maxY = yData.stream().max(Double::compare).orElse(1.0);

        double xRange = maxX - minX;
        double yRange = maxY - minY;

        if (xRange == 0.0) xRange = 1.0;  // Avoid division by zero or scaling issues when all x-values the same.
        if (yRange == 0.0) yRange = 1.0;  // Avoid division by zero or scaling issues when all y-values the same.

        for (int i = 0; i < xData.size(); i++) {
            double x = (xData.get(i) - minX) / xRange * canvasWidth;
            double y = canvasHeight - (yData.get(i) - minY) / yRange * canvasHeight;

            gc.fillOval(x - 3, y - 3, 6, 6); // Draw point
            if(connectPoints && i > 0){
                double prevX = (xData.get(i - 1) - minX) / xRange * canvasWidth;
                double prevY = canvasHeight - (yData.get(i - 1) - minY) / yRange * canvasHeight;
                gc.lineTo(x,y);
                gc.moveTo(x,y); // ensure the path continues with line
            }
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
}
