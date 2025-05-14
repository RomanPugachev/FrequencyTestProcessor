package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "calculatedFrequencyDataRecords")
@DiscriminatorValue(value="calculatedFrequencyDataRecords")
@DiscriminatorColumn(name = "calculationType", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CalculatedFrequencyDataRecord extends FrequencyDataRecord {
    private String calculationName;

    public CalculatedFrequencyDataRecord() {
    }

    public CalculatedFrequencyDataRecord(String calculationName) {
        super();
        this.calculationName = calculationName;
    }

    @Override
    public String getDatasetName() {
        return calculationName;
    }
}
