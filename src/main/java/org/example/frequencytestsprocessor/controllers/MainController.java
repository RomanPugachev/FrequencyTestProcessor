package org.example.frequencytestsprocessor.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.MainApplication;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.CalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.TimeSeriesBasedCalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents.AircraftModel;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.DataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.SensorProxyForTable;
import org.example.frequencytestsprocessor.datamodel.formula.AnalyticalFormula;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;
import org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy.AircraftModelProxy;
import org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy.CalculatedSourceProxy;
import org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy.DataSourceProxy;
import org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy.DataSourceTableProxy;
import org.example.frequencytestsprocessor.services.calculationService.Calculator;
import org.example.frequencytestsprocessor.services.graphsService.GraphsService;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;
import org.example.frequencytestsprocessor.services.refreshingService.Refresher;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFFDataSource;
import org.example.frequencytestsprocessor.services.repositoryService.FRFRepository;
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

//    @FXML
//    private Button changeLanguageButton;

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
    private VBox dataBaseInteractionVBox;

    @FXML
    private VBox dataProcessVBox;

    @FXML
    private TreeTableColumn<AbstractDataset, String> datasetsTreeTableColumn;

    @FXML
    private TreeTableView<AbstractDataset> datasetsTreeTableView;

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

    @FXML
    private Button graphPinButton;

    @FXML
    private NumberAxis graphsXAxisBodeAmplitude;

    @FXML
    private NumberAxis graphsYAxisBodeAmplitude;

    @FXML
    private NumberAxis graphsXAxisBodePhase;

    @FXML
    private NumberAxis graphsYAxisBodePhase;

    @FXML
    private NumberAxis graphsXAxisNyquist;

    @FXML
    private NumberAxis graphsYAxisNyquist;

    @Getter
    @FXML
    private ChoiceBox<String> graphRunChoiceBox;

    @Getter
    @FXML
    private ChoiceBox<String> graphSensorChoiceBox;

    @Getter
    @FXML
    private HBox graphToolBar;

    @Getter
    @FXML
    private ChoiceBox<String> graphTypeChoiceBox;

    @FXML
    private AnchorPane graphsAnchorPane;

    @Getter
    @FXML
    private LineChart<Number, Number> graphsLineChartNyquist;

    @Getter
    @FXML
    private LineChart<Number, Number> graphsLineChartBodeAmplitude;

    @Getter
    @FXML
    private LineChart<Number, Number> graphsLineChartBodePhase;

    @FXML
    private StackPane graphsStackPane;

    @FXML
    private VBox graphsVBox;

    @FXML
    private VBox graphsVBoxBode;

    @FXML
    private Menu languageSettings;

    @FXML
    private MenuItem language_en;

    @FXML
    private MenuItem language_ru;

    @FXML
    private Button loadTimeDataButton;

    @FXML
    private Button loadUFFSourceButton;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private VBox mainVBox;

    @FXML
    private Label manageDataBaseSourcesLabel;

    @FXML
    private HBox manageSourcesHBox;

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
    private HBox sourceAndDatasetsChoiseHBox;

    @FXML
    private TreeTableColumn<DataSourceTableProxy, String> sourcesTreeTableColumn;

    @FXML
    private TreeTableView<DataSourceTableProxy> sourcesTreeTableView;

    @Getter
    @FXML
    private ComboBox<SensorDataType> typeComboBox;
    ////////////////////////////////////////////////////////
    /// Common static objects //////////////////////////////
    @Getter
    public static ObjectMapper objectMapper = new ObjectMapper();

    // Common application parameters and objects //
    private Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs;
    @Getter
    private String currentLanguage = RU;
    @Getter
    private LanguageNotifier languageNotifier;
    private Stage mainStage = Optional.ofNullable(new Stage()).orElseGet(() -> new Stage());
    @Getter
    private MainApplication mainApplication;
    private Refresher refresher = new Refresher(this);
    @Getter
    private IdManager idManager = new IdManager(this);
    private Calculator calculator = new Calculator(this);
    private GraphsService graphsService = new GraphsService(this);
    private FRFRepository frfRepository = FRFRepository.getRepository();
    private UFFDataSource selectedUFFSource;

    public void initializeServices() {
        if (datasetsTreeTableView.getRoot() == null) {
            sourcesTreeTableView.setRoot(new TreeItem<>());

        }
        if (datasetsTreeTableView.getRoot() == null) {
            datasetsTreeTableView.setRoot(new TreeItem<>());
        }
        sourceAndDatasetsChoiseHBox.getChildren().remove(datasetsTreeTableView);
        addCalculatedFRFSourceElement();
        graphsService.initializeService();
        FRFRepository.setInstMainController(this);
        initializeLanguageService();
    }

    public void initializeLanguageService() {
        languageNotifier = new LanguageNotifier(PATH_TO_LANGUAGES + "/mainApplicationLanguage.properties");
        languageNotifier.addObserver( // Adding observers to language notifier with known values for each supported language in props file
                List.of(
                        new LanguageObserverDecorator<>(mainMenuBar),
//                        new LanguageObserverDecorator<>(changeLanguageButton),
                        new LanguageObserverDecorator<>(chosenFileLabel),
                        new LanguageObserverDecorator<>(manageDataBaseSourcesLabel),
                        SensorDataType.DEFAULT_TYPE_LANGUAGE_OBSERVER,
                        Section.DEFAULT_SECTION_LANGUAGE_OBSERVER,
                        (languageProperties, currentLanguage,previousLanguage) -> {
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
                        (languageProperties, currentLanguage, previousLanguage) -> {
                            changeDefaultGraphChoice(graphSensorChoiceBox, DEFAULT_GRAPHS_SENSOR_CHOICE, languageProperties, currentLanguage,previousLanguage);
                        },
                        (languageProperties, currentLanguage, previousLanguage) -> {
                            changeDefaultGraphChoice(graphRunChoiceBox, DEFAULT_GRAPHS_RUN_CHOICE, languageProperties, currentLanguage, previousLanguage);
                        },
                        (languageProperties, currentLanguage, previousLanguage) -> {
                            changeDefaultGraphChoice(graphTypeChoiceBox, DEFAULT_GRAPHS_TYPE_CHOICE + DOT + BODE, languageProperties, currentLanguage, previousLanguage);
                            changeDefaultGraphChoice(graphTypeChoiceBox, DEFAULT_GRAPHS_TYPE_CHOICE + DOT + NYQUIST, languageProperties, currentLanguage, previousLanguage);
//                            changeDefaultGraphChoice(graphTypeChoiceBox, DEFAULT_GRAPHS_TYPE_CHOICE, languageProperties, currentLanguage, previousLanguage);
                        },
                        new LanguageObserverDecorator<>(exportGraphsButton),
                        new LanguageObserverDecorator<>(clearGraphsButton),
                        new LanguageObserverDecorator<>(sourcesTreeTableView),
                        new LanguageObserverDecorator<>(datasetsTreeTableView),
                        (languageProperties, currentLanguage, previousLanguage) -> {
                            ObservableList<TreeItem<DataSourceTableProxy>> dataSources = sourcesTreeTableView.getRoot().getChildren();
                            for(TreeItem<DataSourceTableProxy> curSource : dataSources) {
                                if (curSource.getValue() instanceof CalculatedSourceProxy) {
                                    String decodedText = getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_CALCULATED_DATA_SOURCE + DOT + currentLanguage);
                                    ((CalculatedSourceProxy) curSource.getValue()).setSourceAddress(decodedText);
                                    sourcesTreeTableView.refresh();
                                }
                            }
                        },
                        (languageProperties, currentLanguage, previousLanguage) -> {
                            languageSettings.getItems().forEach(item -> {
                                String languageStringOfItem = item.getId().split("_")[1];
                                if (item.getText().startsWith("* ")) item.setText(item.getText().replace("* ", ""));
                                if (languageStringOfItem.equals(currentLanguage)) {
                                    item.setText("* " + item.getText());
                                }
                            });
                        },
                        observeChartTitle(graphsLineChartBodeAmplitude),
                        observeChartTitle(graphsLineChartBodePhase),
                        observeChartTitle(graphsLineChartNyquist),
                        observeAxis(graphsXAxisBodeAmplitude),
                        observeAxis(graphsYAxisBodeAmplitude),
                        observeAxis(graphsXAxisBodePhase),
                        observeAxis(graphsYAxisBodePhase),
                        observeAxis(graphsXAxisNyquist),
                        observeAxis(graphsYAxisNyquist)
                )
        );
        currentLanguage = EN;
        calculator.setFormulaTable(formulaTable);
    }

    @FXML
    public void updateLanguage() {
        String newLanguage;
        if (currentLanguage.equals(RU)) {
            newLanguage = EN;
        } else {
            newLanguage = RU;
        }
        setCurrentLanguage(newLanguage);
    }

    @FXML
    private void saveUFFSourceFromFileDialog(MouseEvent event) {
        File chosenFile = getFileFromDialog();
        saveUFFSourceFromFile(chosenFile);
    }


    @FXML
    private void saveTimeSeriesSourceFromFileDialog(MouseEvent event) {
        File chosenFile = getFileFromDialog();
        saveTimeSeriesSourceFromFile(chosenFile);
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
        formulaTable.getItems().add((AnalyticalFormula) idManager.addSlave(new AnalyticalFormula()));
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
                new String(languageNotifier.getLanaguagePropertyService()
                        .getProperties().getProperty(CALCULATIONS_DIALOG_TITLE + DOT + currentLanguage)
                        .getBytes(StandardCharsets.ISO_8859_1),
                        StandardCharsets.UTF_8
                )
        );
        tempStage.showAndWait();
    }

    private void performCalculations(Collection<Long> chosenRuns) {
        formulaTable.getItems().forEach(formula -> {
            formula.validate(formula.getFormulaString());
        });
        if (chosenRuns.size() < 1) {
            showAlert("Ошибка", "Ошибка", "Не выбраны запуски для расчета");
            return;
        }
        List<String> idSequence = calculator.getCalculationIdSequence(chosenSensorsTable.getItems().stream().map(s -> ((SensorProxyForTable)s).getId()).collect(Collectors.toList()));
        calculatedFRFs = new HashMap<>();
        String sectionName = sectionComboBox.getValue().getSectionName();
        String typeName = typeComboBox.getValue().getTypeName();
        for (Long runId : chosenRuns) {
            List<Double> frequencies = calculator.getFrequencies(runId);
            calculatedFRFs.put(runId, new HashSet<>());
            for (String id : idSequence) {
                Formula curentFormula = formulaTable.getItems().stream().filter(formula -> formula.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("Cannot find formula with id " + id));
                FRF calculatedFRF = calculator.calculateFRF(runId, id, frequencies, calculatedFRFs);
                calculatedFRFs.get(runId).add(new AbstractMap.SimpleEntry<>(id, calculatedFRF));
                frfRepository.savePipelineCalculatedFrequencyDataRecord(selectedUFFSource, curentFormula, sectionName, typeName, runId, chosenSensorsTable.getItems(), calculatedFRF);
            }
        }
        showSuccess("Success", "Success", "Calculations performed successfully");
        System.out.println(calculatedFRFs);
        graphRunChoiceBox.getItems().clear(); // Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs
        refresher.refreshGraphComboboxes(calculatedFRFs);

    }
    private void performOnlyPossibleCalculations(Collection<Long> chosenRuns) {
        showAlertUnimplemented();
    }


    public void loadImages(){
        try {

//            Image image = mainApplication.getImage("images" + "/ms" + "/processing_status.svg");
//            ImageView imageView = new ImageView(image);
//            imageView.setFitWidth(20);
//            imageView.setFitHeight(20);
//            imageView.setPreserveRatio(true);
//            imageView.setSmooth(true);
//            loadTimeDataButton.setGraphic(imageView);
            Image image = mainApplication.getImage("images" + "/package.jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            loadUFFSourceButton.setGraphic(imageView);

            imageView = new ImageView(mainApplication.getImage("images" + "/pin.png"));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            graphPinButton.setGraphic(imageView);

            imageView = new ImageView(mainApplication.getImage("images" + "/ms" + "/processing_status.png"));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            loadTimeDataButton.setGraphic(imageView);
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
//        assert changeLanguageButton != null : "fx:id=\"changeLanguageButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chooseFileHBox != null : "fx:id=\"chooseFileHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert choseTypeAndSectionHBox != null : "fx:id=\"choseTypeAndSectionHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenFileLabel != null : "fx:id=\"chosenFileLabel\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenSensorsTable != null : "fx:id=\"chosenSensorsTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert chosenSensorsTableContextMenu != null : "fx:id=\"chosenSensorsTableContextMenu\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert clearGraphsButton != null : "fx:id=\"clearGraphsButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert commentToFormulaColumn != null : "fx:id=\"commentToFormulaColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dataBaseInteractionVBox != null : "fx:id=\"dataBaseInteractionVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dataProcessVBox != null : "fx:id=\"dataProcessVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert datasetsTreeTableColumn != null : "fx:id=\"datasetsTreeTableColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert datasetsTreeTableView != null : "fx:id=\"datasetsTreeTableView\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert deleteChosenSensorsMenuItem != null : "fx:id=\"deleteChosenSensorsMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert deleteFormulaMenuItem != null : "fx:id=\"deleteFormulaMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyHBox != null : "fx:id=\"dummyHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert dummyToolBar != null : "fx:id=\"dummyToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert exportGraphsButton != null : "fx:id=\"exportGraphsButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert file != null : "fx:id=\"file\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaAdditionAnalithicalMenuItem != null : "fx:id=\"formulaAdditionAnalithicalMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaAdditionSensorMenuItem != null : "fx:id=\"formulaAdditionSensorMenuItem\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaIdColumn != null : "fx:id=\"formulaIdColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaStringColumn != null : "fx:id=\"formulaStringColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulaTable != null : "fx:id=\"formulaTable\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert formulasContextMenu != null : "fx:id=\"formulasContextMenu\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphPinButton != null : "fx:id=\"graphPinButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphRunChoiceBox != null : "fx:id=\"graphRunChoiceBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphSensorChoiceBox != null : "fx:id=\"graphSensorChoiceBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphToolBar != null : "fx:id=\"graphToolBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphTypeChoiceBox != null : "fx:id=\"graphTypeChoiceBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsAnchorPane != null : "fx:id=\"graphsAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsLineChartBodeAmplitude != null : "fx:id=\"graphsLineChartBodeAmplitude\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsLineChartBodePhase != null : "fx:id=\"graphsLineChartBodePhase\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsLineChartNyquist != null : "fx:id=\"graphsLineChartNyquist\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsStackPane != null : "fx:id=\"graphsStackPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsVBox != null : "fx:id=\"graphsVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert graphsVBoxBode != null : "fx:id=\"graphsVBoxtBode\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert languageSettings != null : "fx:id=\"languageSettings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_en != null : "fx:id=\"language_en\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert language_ru != null : "fx:id=\"language_ru\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert loadTimeDataButton != null : "fx:id=\"loadTimeDataButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert loadUFFSourceButton != null : "fx:id=\"loadUFFSourceButton\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainMenuBar != null : "fx:id=\"mainMenuBar\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert manageDataBaseSourcesLabel != null : "fx:id=\"manageDataBaseSourcesLabel\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert manageSourcesHBox != null : "fx:id=\"manageSourcesHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert processAndVisualizeSplitPane != null : "fx:id=\"processAndVisualizeSplitPane\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sectionComboBox != null : "fx:id=\"sectionComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorIdColumn != null : "fx:id=\"sensorIdColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorNameColumn != null : "fx:id=\"sensorNameColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sensorsChoiseHBox != null : "fx:id=\"sensorsChoiseHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert settings != null : "fx:id=\"settings\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sourceAndDatasetsChoiseHBox != null : "fx:id=\"sourceAndDatasetsChoiseHBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sourcesTreeTableColumn != null : "fx:id=\"sourcesTreeTableColumn\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert sourcesTreeTableView != null : "fx:id=\"sourcesTreeTableView\" was not injected: check your FXML file 'mainScene-view.fxml'.";
        assert typeComboBox != null : "fx:id=\"typeComboBox\" was not injected: check your FXML file 'mainScene-view.fxml'.";

        initializeServices();
        setupWidgetsBehaviour();
        refresher.setDefaultComboBoxes();
        if (System.getenv("PRELOAD_PATH_CSV") != null) {
            File preloadFile = new File(System.getenv("PRELOAD_PATH_CSV"));
            saveTimeSeriesSourceFromFile(preloadFile);
        }
        if (System.getenv("PRELOAD_PATH_UFF") != null) {
            File preloadFile = new File(System.getenv("PRELOAD_PATH_UFF"));
            saveUFFSourceFromFile(preloadFile);
        }
    }

    private void setupWidgetsBehaviour() {
        ////////////////////////////////////////////
        // Setting up of tables and their cells behaviour: https://www.youtube.com/watch?v=GNsBTP2ZXrU, https://stackoverflow.com/questions/22582706/javafx-select-multiple-rows
        sourcesTreeTableView.setShowRoot(false);
        sourcesTreeTableView.getRoot().setExpanded(true);
        // TODO: implement listening of selecting new datasource
        sourcesTreeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DataSourceTableProxy selectedDataSourceProxy = newValue.getValue();
                if (selectedDataSourceProxy instanceof DataSourceProxy) {
                    sourceAndDatasetsChoiseHBox.getChildren().remove(datasetsTreeTableView);
                    DataSource selectedDataSource = ((DataSourceProxy) selectedDataSourceProxy).getDataSource();
                    if (selectedDataSource instanceof UFFDataSource) {
                        UFFDataSource uffSource = (UFFDataSource) selectedDataSource;
                        // Set label which represents current source for calculations
                        chosenFileLabel.setText(uffSource.getSourceAddress());
                        // Delegate further updates to Refresher
                        refresher.refreshOnChangeChosenUFFSource(uffSource);
                        idManager.removeAllSlaves();
                        selectedUFFSource = uffSource;
                    } else if (selectedDataSource instanceof TimeSeriesDataSource) {
//                        TimeSeriesDataSource timeSeriesSource = (TimeSeriesDataSource) selectedDataSource;
                        sourceAndDatasetsChoiseHBox.getChildren().remove(datasetsTreeTableView);
                        callTimeDataSourceDialog((TimeSeriesDataSource) selectedDataSource);
                    }
                } else if (selectedDataSourceProxy.equals(CalculatedSourceProxy.getInstance())) {
                    if (!sourceAndDatasetsChoiseHBox.getChildren().contains(datasetsTreeTableView)) {
                        sourceAndDatasetsChoiseHBox.getChildren().add(datasetsTreeTableView);
                    }
                    // Ensure that datasetsTreeTableView is clear
                    datasetsTreeTableView.getRoot().getChildren().clear();
                    CalculatedSourceProxy.getInstance().getDatasets().forEach(elem -> datasetsTreeTableView.getRoot().getChildren().add(new TreeItem<>(elem)));
                }
            }
        });
        languageSettings.getItems().forEach(item -> {
            String languageStringOfItem = item.getId().split("_")[1];
            item.setOnAction(event -> {
                if (!currentLanguage.equals(languageStringOfItem)) {
                    setCurrentLanguage(languageStringOfItem);
                }
            });
        });
        sourcesTreeTableColumn.setCellValueFactory((datasource) -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return datasource.getValue().getValue().getTableColumnValue();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        datasetsTreeTableView.setShowRoot(false);
        datasetsTreeTableColumn.setCellValueFactory((datasource) -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return datasource.getValue().getValue().getDatasetName();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
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
        graphRunChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            updateLineChart();
        });
        graphSensorChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            updateLineChart();
        });
        graphTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            String graphType = "";
            final String bode = getDecodedProperty(languageNotifier.getLanaguagePropertyService().getProperties(), OTHER + DOT + DEFAULT_GRAPHS_TYPE_CHOICE + DOT + BODE + DOT +currentLanguage);
            final String nyquist = getDecodedProperty(languageNotifier.getLanaguagePropertyService().getProperties(), OTHER + DOT + DEFAULT_GRAPHS_TYPE_CHOICE + DOT + NYQUIST + DOT +currentLanguage);
            if (newValue == null) graphType = BODE;
            else if (newValue.equals(bode)) graphType = BODE;
            else if (newValue.equals(nyquist)) graphType = NYQUIST;
            switchStackPaneLayout(graphType);
        });
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

    // Function of changing language in graphChoiseBox
    private void changeDefaultGraphChoice(ChoiceBox<String> curentChoiceBox, String PROPERTY_ID, Properties languageProperties, String currentLanguage, String previousLanguage) {
        Iterator<String> it = curentChoiceBox.getItems().iterator();
        String previousValue = getDecodedProperty(languageProperties, OTHER + DOT + PROPERTY_ID + DOT + previousLanguage);
        boolean chooseDefault = false;
        while (it.hasNext()) {
            String current = it.next();
            if (current.equals(previousValue)) {
                if (curentChoiceBox.getValue().equals(current)) chooseDefault = true;
                it.remove();
                break;
            }
        }
        String decodedText = getDecodedProperty(languageProperties, OTHER + DOT + PROPERTY_ID + DOT + currentLanguage);
        curentChoiceBox.getItems().add(decodedText);
        if (chooseDefault) curentChoiceBox.setValue(decodedText);
    }

    private void saveUFFSourceFromFile(File chosenFile) {
        if (chosenFile != null && (chosenFile.getAbsolutePath().endsWith(".unv") ||
                chosenFile.getAbsolutePath().endsWith(".uff"))) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("");
            dialog.setHeaderText("Пожалуйста, укажите название модели ЛА, к которому относятся данные:");
            dialog.setContentText("Название модели:");

            // Show the dialog and capture the input
            Optional<String> result = dialog.showAndWait();

            if (!result.isPresent() || result.get().isEmpty()) {
                showAlert("Ошибка", "Ошибка загрузки данных", "Введите название модели ЛА");
                return;
            } else {
                System.out.println("Input received: " + result.get());
            }

            AircraftModel aircraftModel = frfRepository.getAircraftModelByName(result.get(), true);

            UFFDataSource savedSource = frfRepository.saveUFFSource(chosenFile.getAbsolutePath(), aircraftModel);

            insertSourceIntoTable(aircraftModel, savedSource);
//            refresher.refreshUIOnSourceChange(savedSource);
        } else if (chosenFile != null) {
            showAlert("Ошибка", "Ошибка открытия файла", "Попробуйте открыть файл формата .uff");
        }
    }

    private void saveTimeSeriesSourceFromFile(File chosenFile) {
        if (chosenFile != null && chosenFile.getAbsolutePath().endsWith(".csv")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("");
            dialog.setHeaderText("Пожалуйста, укажите название модели ЛА, к которому относятся данные:");
            dialog.setContentText("Название модели:");

            // Show the dialog and capture the input
            Optional<String> result = dialog.showAndWait();

            // Use the result
            if (!result.isPresent() || result.get().isEmpty()) {
                showAlert("Ошибка", "Ошибка загрузки данных", "Введите название модели ЛА");
                return;
            } else {
                System.out.println("Input received: " + result.get());
            }

            AircraftModel aircraftModel = frfRepository.getAircraftModelByName(result.get(), true);

            TimeSeriesDataSource savedSource = frfRepository.saveTimeSeriesSourceFromCSV(chosenFile.getAbsolutePath(), aircraftModel);

            insertSourceIntoTable(aircraftModel, savedSource);
        } else if (chosenFile != null) {
            showAlert("Ошибка", "Ошибка открытия файла", "Попробуйте открыть файл формата .csv");
        }
    }



    public void clearLineChart(MouseEvent event){
        graphsService.clearCharts();
    }

    private void switchStackPaneLayout(String extractedTypeOfGraphs) {
        if ("bode".equalsIgnoreCase(extractedTypeOfGraphs)) {
            graphsVBoxBode.setVisible(true);
            graphsLineChartNyquist.setVisible(false);
        } else if ("nyquist".equalsIgnoreCase(extractedTypeOfGraphs)) {
            graphsVBoxBode.setVisible(false);
            graphsLineChartNyquist.setVisible(true);
        } else {
            System.err.println("Invalid graph type: " + extractedTypeOfGraphs);
        }
    }

    private void updateLineChart(){
        Map<String, FRF> result = new HashMap<>();
        extractFRFForGraphs(result);
        graphsService.updateDataSets(result);
    }

    @FXML
    private void pinCurrentGraph(MouseEvent event){
        Map<String, FRF> result = new HashMap<>();
        extractFRFForGraphs(result);
        Map<String, FRF> pinnedFRFs = graphsService.getPinnedFRFs();
        Set<String> keys = result.keySet().stream().filter(pinnedFRFs::containsKey).collect(Collectors.toSet());
        keys.forEach(key -> {
            pinnedFRFs.remove(key);
            result.remove(key);
        });
        graphsService.pinCurrentGraph(result);
    }

    private void callTimeDataSourceDialog(TimeSeriesDataSource selectedDataSource) {
        FXMLLoader tempLoader = new FXMLLoader(mainApplication.getClass().getResource("fxmls/time_data_source_dialog.fxml"));
        Scene tempScene = null;
        try {
            tempScene = new Scene(tempLoader.load());
            TimeDataSourceDialogController tempController = tempLoader.getController();
            tempController.setTimedialogCommitHandler((parentTimeSeriesDataset, leftLimit, rightLimit, name) -> {
                addTimeSeriesBasedCalculatedFrequencyDataRecord(parentTimeSeriesDataset, leftLimit, rightLimit, name);
            });
            tempController.initializeServices(currentLanguage, selectedDataSource);
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
                        getProperties().getProperty(TIMESERIES_FRF_EXTRACTION_DIALOG_TITLE + DOT + currentLanguage)
                        .getBytes(StandardCharsets.ISO_8859_1),
                        StandardCharsets.UTF_8
                )
        );
        tempStage.showAndWait();
    }

    private void extractFRFForGraphs(Map<String, FRF> result) {
        Long run;
        String sensorStr;
        String defaultRun = getDecodedProperty(languageNotifier.getLanaguagePropertyService().getProperties(), OTHER + DOT + DEFAULT_GRAPHS_RUN_CHOICE + DOT + currentLanguage);
        String defaultSensor = getDecodedProperty(languageNotifier.getLanaguagePropertyService().getProperties(), OTHER + DOT + DEFAULT_GRAPHS_SENSOR_CHOICE + DOT + currentLanguage);
        try {
            run = Long.valueOf(graphRunChoiceBox.getValue());
            sensorStr = graphSensorChoiceBox.getValue();
        } catch (Exception e) {
            System.out.println("Can't update lineChart as no run or sensor selected");
            return;
        }
        if (!(run == null || sensorStr == null || run.equals(defaultRun) || sensorStr.equals(defaultSensor))) {
            Optional<FRF> dataSet = chosenSensorsTable.getItems().stream()
                    .filter(sensor -> ((SensorProxyForTable) sensor).getId().equals(sensorStr))
                    .map(sensor -> (FRF) sensor.getData().get(run))
                    .findFirst();
            if (!dataSet.isPresent()) {
                dataSet = calculatedFRFs.get(run).stream()
                        .filter((entry) -> entry.getKey().equals(sensorStr))
                        .map(entry -> (FRF) entry.getValue())
                        .findFirst();
            }
            result.put(sensorStr + " in run " + run, dataSet
                    .orElseThrow(() -> {
                        showAlert("Error", "Error during extracting data by run and sensor", "No data found for run " + run + " and sensor " + sensorStr);
                        return new RuntimeException("No data found for run " + run + " and sensor " + sensorStr);
                    })
            );
        }
    }

    private void addTimeSeriesBasedCalculatedFrequencyDataRecord(TimeSeriesDataset parentTimeSeriesDataset, Double leftLimit, Double rightLimit, String name) {
        TimeSeriesBasedCalculatedFrequencyDataRecord incomingRec = frfRepository.saveTimeSeriesBasedCalculatedFrequencyDataRecord(parentTimeSeriesDataset, leftLimit, rightLimit, name).orElseThrow(() -> new RuntimeException("Can't save calculated frequency data record"));

        CalculatedSourceProxy.getInstance().getDatasets().add(incomingRec);
    }

    private void addCalculatedFRFSourceElement() {
        sourcesTreeTableView.getRoot().getChildren().add(new TreeItem<>(CalculatedSourceProxy.getInstance()));
    }

    private void setCurrentLanguage(String newLanguage) {
        currentLanguage = newLanguage;
        String newTitle = languageNotifier.getLanaguagePropertyService().getProperties().getProperty(MAIN_APPLICATION_NAME + DOT + currentLanguage);
        if (newTitle != null) {
            byte[] bytes = newTitle.getBytes(StandardCharsets.ISO_8859_1);
            String decodedTitle = new String(bytes, StandardCharsets.UTF_8);
            mainStage.setTitle(decodedTitle);
        }
        languageNotifier.changeLanguage(currentLanguage);
    }

    private void insertSourceIntoTable(AircraftModel parentAircraftModel, DataSource savedSource) {
        TreeItem<DataSourceTableProxy> root = sourcesTreeTableView.getRoot();
        // Check if parentAircraftModel already exists
        TreeItem<DataSourceTableProxy> existingModelItem = root.getChildren().stream()
                .filter(item -> (item.getValue() instanceof AircraftModelProxy) && ((AircraftModelProxy) item.getValue()).getAircraftModel().getAircraftModelName().equals(parentAircraftModel.getAircraftModelName()))
                .findFirst().orElse(null);
        if (existingModelItem == null) {
            // If parentAircraftModel doesn't exist, create a new TreeItem and add it to the root
            existingModelItem = new TreeItem<>(new AircraftModelProxy(parentAircraftModel));
            root.getChildren().add(existingModelItem);
        }
        // Check if savedSource already exists in the parentAircraftModel
        TreeItem<DataSourceTableProxy> existingSourceItem = existingModelItem.getChildren().stream()
                .filter(item -> ((DataSourceTableProxy) item.getValue()).getTableColumnValue().equals(savedSource.getSourceAddress()))
                .findFirst().orElse(null);
        if (existingSourceItem == null) {
            // If savedSource doesn't exist, create a new TreeItem and add it to the parentAircraftModel
            existingSourceItem = new TreeItem<>(new DataSourceProxy(savedSource));
            existingModelItem.getChildren().add(existingSourceItem);
        }
        sourcesTreeTableView.getSelectionModel().select(existingSourceItem);
    }
}