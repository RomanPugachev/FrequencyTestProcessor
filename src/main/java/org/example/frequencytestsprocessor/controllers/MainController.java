package org.example.frequencytestsprocessor.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RuntimeEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import org.example.frequencytestsprocessor.MainApplication;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.*;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;
import org.example.frequencytestsprocessor.services.calculationService.Calculator;
import org.example.frequencytestsprocessor.services.graphsService.GraphsService;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.refreshingService.Refresher;
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
    private MenuItem addAvailableSensorsMenuItem;

    @FXML
    private TableColumn<Sensor, String> availableSensorsColumn;

    @FXML
    private TableView<Sensor> availableSensorsTable;

    @FXML
    private ContextMenu availableSensorsTableContextMenu;

    @FXML
    private Button callPerformCalculationsDialogButton;

    @FXML
    private Button changeLanguageButton;

    @FXML
    private HBox chooseFileHBox;

    @FXML
    private HBox choseTypeAndSectionHBox;

    @FXML
    private Label chosenFileLabel;

    @Getter
    @FXML
    private TableView<Sensor> chosenSensorsTable;

    @FXML
    private ContextMenu chosenSensorsTableContextMenu;

    @FXML
    private Button clearGraphsButton;

    @FXML
    private MenuItem close;

    @FXML
    private TableColumn<Formula, String> commentToFormulaColumn;

    @FXML
    private VBox dataProcessVBox;

    @FXML
    private MenuItem deleteChosenSensorsMenuItem;

    @FXML
    private MenuItem deleteFormulaMenuItem;

    @FXML
    private HBox dummyHBox;

    @FXML
    private ToolBar dummyToolBar;

    @FXML
    private Button exportGraphsButton;

    @FXML
    private Menu file;

    @FXML
    private Button fileDialogButton;

    @FXML
    private MenuItem formulaAdditionAnalithicalMenuItem;

    @FXML
    private MenuItem formulaAdditionSensorMenuItem;

    @FXML
    private TableColumn<Formula, String> formulaIdColumn;

    @FXML
    private TableColumn<Formula, String> formulaStringColumn;

    @FXML
    private TableView<Formula> formulaTable;

    @FXML
    private ContextMenu formulasContextMenu;

    @Getter
    @FXML
    private ChoiceBox<String> graphRunChoiceBox;

    @Getter
    @FXML
    private ChoiceBox<String> graphSensorChoiceBox;

    @Getter
    @FXML
    private HBox graphToolBar;

    @FXML
    private AnchorPane graphsAnchorPane;

    @Getter
    @FXML
    private Canvas graphsCanvas;

    @Getter
    @FXML
    private VBox graphsVBox;

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

    @Getter
    @FXML
    private ComboBox<Section> sectionComboBox;

    @FXML
    private TableColumn<Section, String> sensorIdColumn;

    @FXML
    private TableColumn<Sensor, String> sensorNameColumn;

    @FXML
    private HBox sensorsChoiseHBox;

    @FXML
    private Menu settings;

    @Getter
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
    private Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs;
    @Getter
    private String currentLanguage = RU;
    @Getter
    private LanguageNotifier languageNotifier;
    private Stage mainStage = Optional.ofNullable(new Stage()).orElseGet(() -> new Stage());
    private MainApplication mainApplication;
    private Refresher refresher = new Refresher(this);
    @Getter
    private IdManager idManager = new IdManager(this);
    private Calculator calculator = new Calculator(this);
    private GraphsService graphsService = new GraphsService(this);
    private Map<Long, List<RepresentableDataset>> representableDatasets = new HashMap<>();

    public void initializeServices() {
        initializeLanguageService();
    }

    public void initializeLanguageService() {
        languageNotifier = new LanguageNotifier(PATH_TO_LANGUAGES + "/mainApplicationLanguage.properties");
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
                        new LanguageObserverDecorator<>(chosenSensorsTable),
                        new LanguageObserverDecorator<>(formulaTable),
                        new LanguageObserverDecorator<>(callPerformCalculationsDialogButton),
                        (languageProperties, currentLanguage) -> {
                            changeDefaultGraphChoice(graphSensorChoiceBox, DEFAULT_GRAPHS_SENSOR_CHOICE, languageProperties, currentLanguage);
                        },
                        (languageProperties, currentLanguage) -> {
                            changeDefaultGraphChoice(graphRunChoiceBox, DEFAULT_GRAPHS_RUN_CHOICE, languageProperties, currentLanguage);
                        }
                )
        );
        currentLanguage = RU;
        calculator.setFormulaTable(formulaTable);
        updateLanguage();
        graphsService.initializeService();
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
        setChosenFile(chosenFile);
    }

    @FXML
    private void handleAvailableSensorsTableKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addAvailableSensorsToChosen();
        }
    }

    @FXML
    private void handleChosenSensorsTableKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE){
            removeChosenSensors();
        }
    }

    @FXML
    private void addAvailableSensorsToChosen() {
        ObservableList<Sensor> items = availableSensorsTable.getSelectionModel().getSelectedItems();
        for (Sensor item : items) {
            chosenSensorsTable.getItems().add((SensorProxyForTable) idManager.addSlave(new SensorProxyForTable(item)));
        }
    }

    @FXML
    private void removeChosenSensors() {
        ObservableList<Sensor> selectedItems = chosenSensorsTable.getSelectionModel().getSelectedItems();
        selectedItems.stream().forEach((sens) -> {
            idManager.removeSlave((IdManager.HasId) sens);
        });
        chosenSensorsTable.getItems().removeAll(selectedItems);

    }

    @FXML
    private void addAnalythicalFormula() {
        showAlertUnimplemented();
    }

    @FXML
    public void addSensorBasedFormula() {
        formulaTable.getItems().add((SensorBasedFormula) idManager.addSlave(new SensorBasedFormula()));
    }

    @FXML
    public void deleteFormulaFromTable() {
        ObservableList<Formula> selectedItems = formulaTable.getSelectionModel().getSelectedItems();
        selectedItems.stream().forEach((form) -> {
            idManager.removeSlave((IdManager.HasId) form);
        });
        formulaTable.getItems().removeAll(selectedItems);    }

    @FXML
    private void callPerformCalculationsDialog(MouseEvent event) {
        FXMLLoader tempLoader = new FXMLLoader(mainApplication.getClass().getResource("fxmls/performing_calculations_dialog.fxml"));
        Scene tempScene = null;
        try {
            tempScene = new Scene(tempLoader.load());
            PerformingCalculationsDialogController tempController = tempLoader.getController();
            tempController.setDialogCommitHandler((chosenRuns, showErrors) -> {
                // Here will be handled dialog parameters properly
                if (showErrors) performCalculations(chosenRuns); else performOnlyPossibleCalculations(chosenRuns);
            });
            tempController.initializeServices(currentLanguage, getSharedRuns());
        } catch (IOException e) {
            e.printStackTrace();
            showAlertUnimplemented();
        }
        Stage tempStage = new Stage();
        tempStage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("images/calculator(not_free).jpg")));
        tempStage.initOwner(mainStage);
        tempStage.setScene(tempScene);
        tempStage.setTitle(
                new String(languageNotifier.getLanaguagePropertyService().
                        getProperties().getProperty(CALCULATIONS_DIALOG_TITLE + DOT + currentLanguage)
                        .getBytes(StandardCharsets.ISO_8859_1),
                        StandardCharsets.UTF_8
                )
        );
        tempStage.showAndWait();
    }

    private void performCalculations(Collection<Long> chosenRuns) {
        // TODO: debug this
        formulaTable.getItems().forEach(formula -> {
            formula.validate(formula.getFormulaString());
        });
        if (chosenRuns.size() < 1) {
            showAlert("Ошибка", "Ошибка", "Не выбраны запуски для расчета");
            return;
        }
        List<String> idSequence = calculator.getCalculationIdSequence(chosenSensorsTable.getItems().stream().map(s -> ((SensorProxyForTable)s).getId()).collect(Collectors.toList()));
        calculatedFRFs = new HashMap<>();
        for (Long runId : chosenRuns) {
            List<Double> frequencies = calculator.getFrequencies(runId);
            calculatedFRFs.put(runId, new HashSet<>());
            for (String id : idSequence) {
                calculatedFRFs.get(runId).add(new AbstractMap.SimpleEntry<>(id, calculator.calculateFRF(runId, id, frequencies, calculatedFRFs)));
            }
        }
        showSuccess("Success", "Success", "Calculations performed successfully");
        System.out.println(calculatedFRFs);
        graphRunChoiceBox.getItems().clear(); //Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs
        // TODO: implement adding of calculated FRFs in choice boxes. Implement visualization chosen options
        refresher.refreshGraphComboboxes(calculatedFRFs);
//        graphRunChoiceBox.getItems().addAll(calculatedFRFs.keySet());
//        graphSensorChoiceBox.getItems().
//        graphSensorChoiceBox.getItems().add
//        graphSensorChoiceBox.
    }
    private void performOnlyPossibleCalculations(Collection<Long> chosenRuns) {
        showAlertUnimplemented();
    }


    public void loadImages(){
        try {
            Image image = mainApplication.getImage("images/" + "package.jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            fileDialogButton.setGraphic(imageView);

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        assert addAvailableSensorsMenuItem != null : "fx:id=\"addAvailableSensorsMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert availableSensorsColumn != null : "fx:id=\"availableSensorsColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert availableSensorsTable != null : "fx:id=\"availableSensorsTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert availableSensorsTableContextMenu != null : "fx:id=\"availableSensorsTableContextMenu\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert callPerformCalculationsDialogButton != null : "fx:id=\"callPerformCalculationsDialogButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert changeLanguageButton != null : "fx:id=\"changeLanguageButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chooseFileHBox != null : "fx:id=\"chooseFileHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert choseTypeAndSectionHBox != null : "fx:id=\"choseTypeAndSectionHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenFileLabel != null : "fx:id=\"chosenFileLabel\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenSensorsTable != null : "fx:id=\"chosenSensorsTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenSensorsTableContextMenu != null : "fx:id=\"chosenSensorsTableContextMenu\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert clearGraphsButton != null : "fx:id=\"clearGraphsButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert commentToFormulaColumn != null : "fx:id=\"commentToFormulaColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dataProcessVBox != null : "fx:id=\"dataProcessVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert deleteChosenSensorsMenuItem != null : "fx:id=\"deleteChosenSensorsMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert deleteFormulaMenuItem != null : "fx:id=\"deleteFormulaMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyHBox != null : "fx:id=\"dummyHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyToolBar != null : "fx:id=\"dummyToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert exportGraphsButton != null : "fx:id=\"exportGraphsButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert file != null : "fx:id=\"file\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert fileDialogButton != null : "fx:id=\"fileDialogButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaAdditionAnalithicalMenuItem != null : "fx:id=\"formulaAdditionAnalithicalMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaAdditionSensorMenuItem != null : "fx:id=\"formulaAdditionSensorMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaIdColumn != null : "fx:id=\"formulaIdColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaStringColumn != null : "fx:id=\"formulaStringColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaTable != null : "fx:id=\"formulaTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulasContextMenu != null : "fx:id=\"formulasContextMenu\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphRunChoiceBox != null : "fx:id=\"graphRunChoiceBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphSensorChoiceBox != null : "fx:id=\"graphSensorChoiceBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphToolBar != null : "fx:id=\"graphToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsAnchorPane != null : "fx:id=\"graphsAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsCanvas != null : "fx:id=\"graphsCanvas\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsVBox != null : "fx:id=\"graphsVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert languageSettings != null : "fx:id=\"languageSettings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_en != null : "fx:id=\"language_en\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_ru != null : "fx:id=\"language_ru\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainMenuBar != null : "fx:id=\"mainMenuBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert processAndVisualizeSplitPane != null : "fx:id=\"processAndVisualizeSplitPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sectionComboBox != null : "fx:id=\"sectionComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorIdColumn != null : "fx:id=\"sensorIdColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorNameColumn != null : "fx:id=\"sensorNameColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorsChoiseHBox != null : "fx:id=\"sensorsChoiseHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert settings != null : "fx:id=\"settings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert typeComboBox != null : "fx:id=\"typeComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";

        initializeServices();
        setupWidgetsBehaviour();
        refresher.setDefaultComboBoxes();
        if (System.getenv("PRELOAD_PATH") != null) {
            File preloadFile = new File(System.getenv("PRELOAD_PATH"));
            setChosenFile(preloadFile);
        }
    }

    private void setupWidgetsBehaviour() {
        availableSensorsColumn.setCellValueFactory(new PropertyValueFactory<>("sensorName"));
        availableSensorsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sensorNameColumn.setCellValueFactory(new PropertyValueFactory<>("sensorName"));
        sensorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
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
                chosenSensorsTable.getItems().clear();
                availableSensorsTable.getItems().addAll(sensors);
            }
        });
        chosenSensorsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        formulaTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        formulaStringColumn.setCellValueFactory(new PropertyValueFactory<>("formulaString"));
        formulaStringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        formulaStringColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Formula, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Formula, String> event) {
                Formula editingFormula = event.getRowValue();
                if (editingFormula.validate(event.getNewValue())) {
                    editingFormula.setFormulaString(event.getNewValue());
                } else {
                    editingFormula.setFormulaString(event.getOldValue());
                    event.getTableView().refresh();
                }
            }
        });
        formulaIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        formulaIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        formulaIdColumn.setOnEditCommit(event -> idManager.handleIdUpdate().handle(event));
        commentToFormulaColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        commentToFormulaColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private List<Long> getSharedRuns() {
        var items = chosenSensorsTable.getItems();
        if (items.size() > 0) {
            Set<Long> sharedRuns=new HashSet<>();
            items.getFirst().getData().keySet().forEach(sharedRuns::add);
            items.forEach(sens -> sharedRuns.retainAll(sens.getData().keySet()));
            return sharedRuns.stream().toList();
        } else { return new ArrayList<>(); }
    }

    private void setChosenFile(File chosenFile) {
        if (chosenFile != null && (chosenFile.getAbsolutePath().endsWith(".unv") ||
                chosenFile.getAbsolutePath().endsWith(".uff"))) {
            this.chosenFile = chosenFile;
            chosenFileLabel.setText(chosenFile.getAbsolutePath());
            this.uff = UFF.readUNVFile(this.chosenFile.getAbsolutePath());
            refresher.refreshOnChangeFilePath();
        } else if (chosenFile != null) {
            showAlert("Ошибка", "Ошибка открытия файла", "Попробуйте открыть файл формата .uff");
        }
    }

    private void changeDefaultGraphChoice(ChoiceBox<String> curentChoiceBox, String PROPERTY_ID, Properties languageProperties, String currentLanguage) {
        Iterator<String> it = curentChoiceBox.getItems().iterator();
        while (it.hasNext()) {
            String current = it.next();
            if (current.endsWith("...")) {
                it.remove();
            }
            break;
        }
        curentChoiceBox.getItems().add(languageProperties.getProperty(OTHER + DOT + PROPERTY_ID + DOT + currentLanguage));
    }
}