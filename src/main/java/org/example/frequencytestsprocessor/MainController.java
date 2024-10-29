package org.example.frequencytestsprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.UFF58Representation;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;
import org.example.frequencytestsprocessor.services.uffFilesProcService.UFF;
import org.example.frequencytestsprocessor.widgetsDecoration.LanguageObserverDecorator;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.*;
import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

@Setter
public class MainController {

////Objects of interface//////////////
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Sensor, String> availableSensorsColumn;

    @FXML
    private TableView availableSensorsTable;

    @FXML
    private Button changeLanguageButton;

    @FXML
    private HBox chooseFileHBox;

    @FXML
    private HBox choseTypeAndSectionHBox;

    @FXML
    private Label chosenFileLabel;

    @FXML
    private TableView<?> chosenSensorsTable;

    @FXML
    private MenuItem close;

    @FXML
    private VBox dataProcessVBox;

    @FXML
    private HBox dummyHBox;

    @FXML
    private ToolBar dummyToolBar;

    @FXML
    private Menu file;

    @FXML
    private Button fileDialogButton;

    @FXML
    private AnchorPane graphsAnchorPane;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private Menu languageSettings;

    @FXML
    private MenuItem language_en;

    @FXML
    private MenuItem language_ru;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private VBox mainVBox;

    @FXML
    private SplitPane processAndVisualizeSplitPane;

    @FXML
    private ComboBox<Section> sectionComboBox;

    @FXML
    private TableColumn<?, ?> sensorNameColumn;

    @FXML
    private HBox sensorsChoiseHBox;

    @FXML
    private Menu settings;

    @FXML
    private ComboBox<SensorDataType> typeComboBox;
////////////////////////////////////////////////////////
/// Common static objects //////////////////////////////
    @Getter
    public static ObjectMapper objectMapper = new ObjectMapper();

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
                        new LanguageObserverDecorator<>(changeLanguageButton),
                        new LanguageObserverDecorator<>(chosenFileLabel),
                        SensorDataType.DEFAULT_TYPE_LANGUAGE_OBSERVER,
                        Section.DEFAULT_SECTION_LANGUAGE_OBSERVER,
                        (languageProperties, currentLanguage) -> {
                            Section currentSection = sectionComboBox.getValue();
                            SensorDataType currentType = typeComboBox.getValue();
                            sectionComboBox.getItems().remove(currentSection);
                            sectionComboBox.getItems().add(currentSection);
                            sectionComboBox.setValue(currentSection);
                            typeComboBox.setValue(currentType);
                        },
                        new LanguageObserverDecorator<>(availableSensorsTable),
                        new LanguageObserverDecorator<>(chosenSensorsTable)
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
            this.uff = UFF.readUNVFile(this.chosenFile.getAbsolutePath());
            refresher.refreshOnChangeFilePath();
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
        assert availableSensorsColumn != null : "fx:id=\"availableSensorsColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert availableSensorsTable != null : "fx:id=\"availableSensorsTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert changeLanguageButton != null : "fx:id=\"changeLanguageButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chooseFileHBox != null : "fx:id=\"chooseFileHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert choseTypeAndSectionHBox != null : "fx:id=\"choseTypeAndSectionHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenFileLabel != null : "fx:id=\"chosenFileLabel\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenSensorsTable != null : "fx:id=\"chosenSensorsTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dataProcessVBox != null : "fx:id=\"dataProcessVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyHBox != null : "fx:id=\"dummyHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyToolBar != null : "fx:id=\"dummyToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert file != null : "fx:id=\"file\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert fileDialogButton != null : "fx:id=\"fileDialogButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsAnchorPane != null : "fx:id=\"graphsAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert idColumn != null : "fx:id=\"idColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert languageSettings != null : "fx:id=\"languageSettings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_en != null : "fx:id=\"language_en\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_ru != null : "fx:id=\"language_ru\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainMenuBar != null : "fx:id=\"mainMenuBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert processAndVisualizeSplitPane != null : "fx:id=\"processAndVisualizeSplitPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sectionComboBox != null : "fx:id=\"sectionComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorNameColumn != null : "fx:id=\"sensorNameColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorsChoiseHBox != null : "fx:id=\"sensorsChoiseHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert settings != null : "fx:id=\"settings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert typeComboBox != null : "fx:id=\"typeComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        initializeServices();
        setupWidgetsBehaviour();
        refresher.setDefaultComboBoxes();
    }

    private void setupWidgetsBehaviour() {
        availableSensorsColumn.setCellValueFactory(new PropertyValueFactory<>("sensorName"));
        sectionComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<SensorDataType> sensorDataTypes = FXCollections.observableArrayList(newValue.getTypes());
                typeComboBox.setItems(sensorDataTypes);
                typeComboBox.setValue(SensorDataType.DEFAULT_TYPE);
            }
        });
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<Sensor> sensors = FXCollections.observableArrayList(newValue.getSensors());
                availableSensorsTable.getItems().clear();
                availableSensorsTable.getItems().addAll(sensors);
            }
        });
        availableSensorsTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    print("You pressed enter!");
                    var items = availableSensorsTable.getSelectionModel().getSelectedItems();
                    for (var item : items) {
                        print(item);
                    }
                }
            }
        });
    }

    private class Refresher {
        private void refreshOnChangeFilePath() {
            setDefaultComboBoxes();
            UFF58Representation currentRepresentation;
            for (UFF58 currentUFF58 : uff) {
                try {
                    currentRepresentation = new UFF58Representation(currentUFF58); final Section currentSectionInRepr = currentRepresentation.section;
                    final SensorDataType currentTypeRepresentation = currentRepresentation.sensorDataType;
                    if (!sectionComboBox.getItems().contains(currentRepresentation.section)) {
                        sectionComboBox.getItems().add(currentRepresentation.section);
                    } Section currentSection = sectionComboBox.getItems().stream().filter(section -> section.equals(currentSectionInRepr)).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find section by representation"));
                    if (!currentSection.getTypes().contains(currentRepresentation.sensorDataType)) {
                        currentSection.addType(currentRepresentation.sensorDataType);
                    } SensorDataType currentType = currentSection.getTypes().stream().filter(type -> type.equals(currentTypeRepresentation)).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find type in current section"));
                    currentType.addSensor(currentRepresentation.sensorWithData);
                } catch (Exception e) {
                    print("Couldn't create representation of UFF58 dataset, because:\n" + e.getMessage());
                }
            }
        }

        private void setDefaultComboBoxes() {
            sectionComboBox.getItems().clear();
            sectionComboBox.getItems().add(Section.DEFAULT_SECTION);
            sectionComboBox.setValue(sectionComboBox.getItems().getFirst());
            typeComboBox.setValue(typeComboBox.getItems().getFirst());
        }
    }
}
