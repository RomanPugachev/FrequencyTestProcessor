package org.example.frequencytestsprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jep.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.uffFilesProcService.UFF;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;
import org.example.frequencytestsprocessor.services.PythonInterpreterService;

import java.io.*;
import java.lang.reflect.Executable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.example.frequencytestsprocessor.commons.CommonMethods.getFileFromDialog;
import static org.example.frequencytestsprocessor.commons.CommonMethods.printByteArrayOutputStram;
import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

@Setter
public class MainController {

    //Objects of interface
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
    private Button changeLanguageButton;

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

    // Common static objects
    @Getter
    private static ObjectMapper objectMapper = new ObjectMapper();

    // Common application parameters
    private File chosenFile;
    @Getter
    private UFF uff;
    @Getter
    private String currentLanguage = RU;
    private LanguageNotifier languageNotifier;
    private Stage mainStage = Optional.ofNullable(new Stage()).orElseGet(() -> new Stage());
    private Refresher refresher = this.new Refresher();

    public void initializeServices() {
        initializeLanguageService();
    }

    public void initializeLanguageService() {
        languageNotifier = new LanguageNotifier();
        languageNotifier.addObserver(
                List.of(
                        new LanguageObserverDecorator<>(mainMenuBar),
                        new LanguageObserverDecorator(changeLanguageButton),
                        new LanguageObserverDecorator(chosenFileLabel),
                        new LanguageObserverDecorator(sectionComboBox),
                        new LanguageObserverDecorator(typeComboBox)
                )
        );
        currentLanguage = RU;
        updateLanguage();
    }

    @FXML
    private void updateLanguage() {
        if (currentLanguage.equals(RU)) {
            currentLanguage = EN;
        } else {
            currentLanguage = RU;
        }
        String newTitle = languageNotifier.getLanaguagePropertyService().getProperties().getProperty(MAIN_APPLICATION_NAME + DOT + currentLanguage);
        if (newTitle != null) {
            byte[] bytes = newTitle.getBytes(StandardCharsets.ISO_8859_1);
            String decodedTitle = new String(bytes, StandardCharsets.UTF_8);
            mainStage.setTitle(decodedTitle);
        }
        languageNotifier.changeLanguage(currentLanguage);
    }

    @FXML
    private void callFileDialog(MouseEvent event) {
        File chosenFile = getFileFromDialog();
        if (chosenFile != null && (chosenFile.getAbsolutePath().endsWith(".unv") ||
                chosenFile.getAbsolutePath().endsWith(".uff"))) {
            chosenFileLabel.setText(chosenFile.getAbsolutePath());
            this.chosenFile = chosenFile;
            this.uff = UFF.readUNVFile(this.chosenFile.getAbsolutePath(), objectMapper);
        } else if (chosenFile != null) {
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
        assert changeLanguageButton != null : "fx:id=\"changeLanguageButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
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
        initializeServices();
        callMyPythonScript();
    }

    private void callMyPythonScript() {
         // Initialize the JEP shared interpreter
        Jep pythonInterpreter = PythonInterpreterService.getPythonInterpreter();
        ByteArrayOutputStream pythonOutput = PythonInterpreterService.getPythonOutputStream();
        String pythonScript = CommonMethods.getTextFileContent(PATH_OF_PYTHON_SCRIPT_FOR_UFF);
        pythonInterpreter.exec(pythonScript);
        pythonInterpreter.exec(String.format("parse_UFF('%s')", "C:\\\\Temp\\\\test_uff.uff"));
        try {
            printByteArrayOutputStram(pythonOutput);
        } catch (IOException e) {
            throw new RuntimeException("Unable to print python output from Main controller", e.getCause());
        }
    }

    private class Refresher {
        private void refreshOnChangeFilePath() {
            System.out.printf("Refresher.refreshOnChangeFilePath(): new file is %s", MainController.this.chosenFile);
        }
    }
}
