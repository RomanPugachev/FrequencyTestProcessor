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
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations.ButtonLanguageObserver;
import org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations.ComboBoxLanguageObserver;
import org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations.LabelLanguageObserver;
import org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations.MenuBarLanguageObserver;
import org.example.frequencytestsprocessor.services.uffFilesProcService.UFF;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.example.frequencytestsprocessor.commons.CommonMethods.getFileFromDialog;
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

    public void initializeServices() {
        initializeLanguageService();
    }

    public void initializeLanguageService() {
        languageNotifier = new LanguageNotifier();
        languageNotifier.addObserver(
                List.of(
                        new MenuBarLanguageObserver(mainMenuBar),
                        new ButtonLanguageObserver(changeLanguageButton),
                        new LabelLanguageObserver(chosenFileLabel),
                        new ComboBoxLanguageObserver(sectionComboBox),
                        new ComboBoxLanguageObserver(typeComboBox)
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
        try (ByteArrayOutputStream jepOuputStream = new ByteArrayOutputStream();
             Jep jep = new JepConfig().redirectStdout(jepOuputStream).createSubInterpreter();){
            /*
            jep.exec("import numpy as np\n" +
                    "import os\n" +
                    "import sys\n" +
                    "uffModuleDir = os.getcwd() + \\\\ + ...");
            jep.exec("sys.path.insert(1, f'/path/to/application/app/folder')\n" +
                    "import file");
            jep.exec("import FrequencyTestsProcessor.src.main.java.org.example.frequencytestsprocessor.services.uffFilesProcService.pythonSource.UFFReaderApp as UFFReadingUtils");
            jep.exec("from java.lang import System");
            jep.exec("print('Hello from Python')\n" +
                    "print('Hello from Python')");
            jep.exec("current_directory = os.getcwd()\n" +
                    "print(f\"Current directory: {current_directory}\")");
            */
            jep.exec("import sys\n" +
                    "import json\n" +
                    "import pyuff\n" +
                    "import numpy as np\n" +
                    "import os\n" +
                    "\n" +
                    "def jsonalzie_list_with_complex(complex_list):\n" +
                    "    return [{\"real\": possible_complex.real, \"imag\": possible_complex.imag} if isinstance(possible_complex, complex) else possible_complex\n" +
                    "            for possible_complex in complex_list]\n" +
                    "\n" +
                    "def jsonalize_set(incoming_set):\n" +
                    "    return {k: jsonalzie_list_with_complex(v.tolist()) if isinstance(v, np.ndarray) else v\n" +
                    "            for k, v in incoming_set.items()}\n" +
                    "\n" +
                    "\n" +
                    "def parse_UFF(file_path):\n" +
                    "    unv_file = pyuff.UFF(file_path)\n" +
                    "    print(str(unv_file.get_set_types())[1:-1])\n" +
                    "    for i in range(unv_file.get_n_sets()):\n" +
                    "        current_set = unv_file.read_sets(i)\n" +
                    "        jsonalizable_dict = jsonalize_set(current_set)\n" +
                    "        print(json.dumps(jsonalizable_dict))\n" +
                    "        print(\"END_OF_JSON\")\n" +
                    "parse_UFF('C:\\\\Temp\\\\test_uff.uff');");
            printByteArrayOutputStram(jepOuputStream);
        } catch (JepException e){
            e.printStackTrace();
            System.out.println("EXECUTION WITH ERROR");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("EXECUTION WITH ERROR");
        }
    }

    private static void printByteArrayOutputStram(ByteArrayOutputStream jepOuputStream) throws IOException {
        var resultByteArray = jepOuputStream.toByteArray();
        try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resultByteArray)))){
            while (inputStreamReader.ready()) {
                System.out.println(inputStreamReader.readLine());
            }
        }
    }
}
