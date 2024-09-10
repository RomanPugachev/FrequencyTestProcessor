package org.example.frequencytestsprocessor.commons;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CommonMethods {

    public static File getFileFromDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с данными");
        return fileChooser.showOpenDialog(new Stage());
    }
}
