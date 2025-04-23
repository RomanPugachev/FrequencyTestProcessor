package org.example.frequencytestsprocessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.frequencytestsprocessor.controllers.MainController;

import java.io.IOException;
// TODO: list:
// TODO: 0. Fix calculation of formula, based on other formula +
// TODO: 1. Handle bug with disappearing data points +
// TODO: 2. Add scaling of fourier transformed data chat +- (need to add rectangle of selecting area)
// TODO: 3. Enable to set left and right limits of x axis by selecting area
// TODO: 4. Add global setting dialog
// TODO: 5. Add possibility to save datasets
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxmls/mainScene-view.fxml"));
        Parent root = loader.load();

//        TODO: improve following scheme https://drawsql.app/teams/sss-81/diagrams/ftp-scheme
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