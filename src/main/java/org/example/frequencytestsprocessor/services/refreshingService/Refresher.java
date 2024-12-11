package org.example.frequencytestsprocessor.services.refreshingService;

import lombok.AllArgsConstructor;
import org.example.frequencytestsprocessor.MainController;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr.UFF58Representation;

import static org.example.frequencytestsprocessor.commons.CommonMethods.print;

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
    }

    public void refreshIdsInTables(){
        mainController.getIdManager().refreshAllIds();
    }
}
