package org.example.frequencytestsprocessor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox chooseFileHBox;

    @FXML
    private HBox choseTypeAndSectionHBox;

    @FXML
    private Label chosenFileLabel;

    @FXML
    private VBox dataProcessVBox;

    @FXML
    private Button dummyButton;

    @FXML
    private HBox dummyHBox;

    @FXML
    private ToolBar dummyToolBar;

    @FXML
    private Button fileDialogButton;

    @FXML
    private AnchorPane graphsAnchorPane;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private VBox mainVBox;

    @FXML
    private SplitPane processAndVisualizeSplitPane;

    @FXML
    private ComboBox<?> sectionComboBox;

    @FXML
    private ComboBox<?> typeComboBox;

    private File chosenFile;

    @FXML
    private void callFileDialog(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с данными");
        File chosenFile = fileChooser.showOpenDialog(new Stage());
        if (chosenFile != null && chosenFile.getAbsolutePath().endsWith(".unv")){
            chosenFileLabel.setText(chosenFile.getAbsolutePath());
            this.chosenFile = chosenFile;
        } else if(chosenFile != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка открытия файла");
            alert.setContentText("Попробуйте открыть файл формата .unv");
            alert.showAndWait();
        }

    }

    @FXML
    void initialize() {
        assert chooseFileHBox != null : "fx:id=\"chooseFileHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert choseTypeAndSectionHBox != null : "fx:id=\"choseTypeAndSectionHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenFileLabel != null : "fx:id=\"chosenFileLabel\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dataProcessVBox != null : "fx:id=\"dataProcessVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyButton != null : "fx:id=\"dummyButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyHBox != null : "fx:id=\"dummyHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyToolBar != null : "fx:id=\"dummyToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert fileDialogButton != null : "fx:id=\"fileDialogButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsAnchorPane != null : "fx:id=\"graphsAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainMenuBar != null : "fx:id=\"mainMenuBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert processAndVisualizeSplitPane != null : "fx:id=\"processAndVisualizeSplitPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sectionComboBox != null : "fx:id=\"sectionComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert typeComboBox != null : "fx:id=\"typeComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";

    }

}
