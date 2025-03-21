package org.example.frequencytestsprocessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.services.PythonInterpreterService;

import java.io.IOException;
import java.util.List;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxmls/mainScene-view.fxml"));
        System.setProperty("jep.pythonExecutable", findPython());
        PythonInterpreterService.JepLoader.loadJep();
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

    public static String findPython() {
        List<String> commands = List.of("python3", "python", "py");
        for (String cmd : commands) {
            try {
                Process process = new ProcessBuilder(cmd, "--version").start();
                process.waitFor();
                return cmd;
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("Python not found!");
    }
}