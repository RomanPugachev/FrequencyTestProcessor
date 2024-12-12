package org.example.frequencytestsprocessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.frequencytestsprocessor.controllers.MainController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxmls/mainScene-view.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.setMainStage(stage);
        controller.setMainApplication(this);
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("images/logo.png")));
        stage.setTitle("Frequency tests processor");
        stage.setScene(scene);
        stage.show();
        controller.loadImages();
    }

    public Image getImage(String path) {
        return new Image(MainApplication.class.getResourceAsStream(path));
    }

    public static void main(String[] args) {
        launch();
    }
}