package org.example.frequencytestsprocessor.services.graphsService;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.frequencytestsprocessor.controllers.MainController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GraphsService {
    private MainController mainController;

    public void generateExample(int numberOfPoints, int linearCoefficient){
        Canvas canvas = mainController.getGraphsCanvas();
        List<Double> xData = new ArrayList<>(numberOfPoints);
        List<Double> yData = new ArrayList<>(numberOfPoints);
        for (int i=0; i<numberOfPoints;i++){
            xData.add(i * 1.0);
            yData.add(linearCoefficient * xData.get(i));
        }
        mainController.getGraphsCanvas().getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        mainController.getGraphsCanvas().getGraphicsContext2D().setStroke(Paint.valueOf("blue"));
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, 0, 0, canvas.getHeight());
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight());
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, 0, 0, canvas.getHeight());
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight());
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
        mainController.getGraphsCanvas().getGraphicsContext2D().strokeLine(0, 0, 0, canvas.getHeight());
    }
}
