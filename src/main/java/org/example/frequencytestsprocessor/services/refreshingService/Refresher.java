package org.example.frequencytestsprocessor.services.refreshingService;

import lombok.AllArgsConstructor;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.*;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;

import java.util.Map;
import java.util.Set;

import static org.example.frequencytestsprocessor.commons.CommonMethods.getDecodedProperty;
import static org.example.frequencytestsprocessor.commons.CommonMethods.print;
import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

@AllArgsConstructor
public class Refresher {
    private MainController mainController;
    public void refreshOnChangeFilePath() {
        var sectionComboBox = mainController.getSectionComboBox();
        var uff = mainController.getUff();


        setDefaultComboBoxes();
        UFF58Representation currentRepresentation;
        for (UFF58 currentUFF58 : uff) {
            try {
                currentRepresentation = new UFF58Representation(currentUFF58); final Section currentSectionInRepr = currentRepresentation.section;
                final SensorDataType currentTypeRepresentation = currentRepresentation.sensorDataType;
                Sensor currentSensorRepresentation = currentRepresentation.sensorWithData;
                if (!sectionComboBox.getItems().contains(currentRepresentation.section)) {
                    sectionComboBox.getItems().add(currentRepresentation.section);
                } Section currentSection = sectionComboBox.getItems().stream().filter(section -> section.equals(currentSectionInRepr)).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find section by representation"));
                if (!currentSection.getTypes().contains(currentRepresentation.sensorDataType)) {
                    currentSection.addType(currentRepresentation.sensorDataType);
                } SensorDataType currentType = currentSection.getTypes().stream().filter(type -> type.equals(currentTypeRepresentation)).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find type in current section"));
                if (!currentType.getSensors().contains(currentRepresentation.sensorWithData)){
                    currentType.addSensor(currentRepresentation.sensorWithData);
                } Sensor currentSensor = currentType.getSensors().stream().filter(sensor -> sensor.equals(currentSensorRepresentation)).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find sensor"));
                currentSensor.mergeSensorData(currentSensorRepresentation);
            } catch (Exception e) {
                print("Couldn't create representation of UFF58 dataset, because:\n" + e.getMessage());
            }
        }
    }

    public void setDefaultComboBoxes() {
        var sectionComboBox = mainController.getSectionComboBox();
        var typeComboBox = mainController.getTypeComboBox();

        sectionComboBox.getItems().clear();
        sectionComboBox.getItems().add(Section.DEFAULT_SECTION);
        sectionComboBox.setValue(sectionComboBox.getItems().getFirst());
        typeComboBox.setValue(typeComboBox.getItems().getFirst());
        refreshGraphComboboxes();
    }

    public void refreshGraphComboboxes() {
        var graphSensorChoiceBox = mainController.getGraphSensorChoiceBox();
        var graphRunChoiceBox = mainController.getGraphRunChoiceBox();
        var graphTypeChoiceBox = mainController.getGraphTypeChoiceBox();
        var languageProperties = mainController.getLanguageNotifier().getLanaguagePropertyService().getProperties();

        graphSensorChoiceBox.getItems().clear();
        graphSensorChoiceBox.getItems().add(getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_GRAPHS_SENSOR_CHOICE + DOT + mainController.getCurrentLanguage()));
        graphSensorChoiceBox.setValue(graphSensorChoiceBox.getItems().getFirst());
        graphRunChoiceBox.getItems().clear();
        graphRunChoiceBox.getItems().add(getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_GRAPHS_RUN_CHOICE + DOT + mainController.getCurrentLanguage()));
        graphRunChoiceBox.setValue(graphRunChoiceBox.getItems().getFirst());
        graphTypeChoiceBox.getItems().clear();
        graphTypeChoiceBox.getItems().add(getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_GRAPHS_TYPE_CHOICE + DOT + mainController.getCurrentLanguage()));
        graphTypeChoiceBox.setValue(graphTypeChoiceBox.getItems().getFirst());
        graphTypeChoiceBox.getItems().add(getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_GRAPHS_TYPE_CHOICE + DOT + BODE + DOT + mainController.getCurrentLanguage()));
        graphTypeChoiceBox.getItems().add(getDecodedProperty(languageProperties, OTHER + DOT + DEFAULT_GRAPHS_TYPE_CHOICE + DOT + NYQUIST + DOT + mainController.getCurrentLanguage()));

    }

    public void refreshGraphComboboxes(Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs) {
        refreshGraphComboboxes();
        var graphSensorChoiceBox = mainController.getGraphSensorChoiceBox();
        var graphRunChoiceBox = mainController.getGraphRunChoiceBox();
        var graphTypeChoiceBox = mainController.getGraphTypeChoiceBox();


        calculatedFRFs.keySet().forEach(run -> graphRunChoiceBox.getItems().add(run.toString()));
        calculatedFRFs.get(calculatedFRFs.keySet().stream().findFirst().orElseThrow()).forEach(entry -> graphSensorChoiceBox.getItems().add(entry.getKey()));


        var chosenSensorsTable = mainController.getChosenSensorsTable();
        chosenSensorsTable.getItems().forEach(sensor -> {
            graphSensorChoiceBox.getItems().add(((SensorProxyForTable) sensor).getId());
        });
    }

    public void refreshIdsInTables(){
        mainController.getIdManager().refreshAllIds();
    }
}
