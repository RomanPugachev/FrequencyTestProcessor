package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.converters.ComplexListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

import static org.example.frequencytestsprocessor.controllers.MainController.objectMapper;

@Getter
@Entity
@Table(name = "uffBasedFRFs")
@DiscriminatorValue(value = "UFFBasedFRF")
public class UFFBasedFRF extends FrequencyDataRecord {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentUffId", referencedColumnName = "uff_dataset_id")
    private UFF58 parentUff58;

    @Override
    public void refreshRawFrequencyData() {
        setRawFrequencyData(new RawFrequencyData(parentUff58.getFrequencies(), parentUff58.getComplexValues()));
    }

    public String getDatasetName() {
        return parentUff58.getDatasetName();
    }

    public UFFBasedFRF() {
    }

    public UFFBasedFRF(UFF58 uff58) {
        this.parentUff58 = uff58;
        uff58.setUffBasedFRF(this);
    }

    public void setParentUFF58Dataset(UFF58 uff58) {
        parentUff58 = uff58;
//        if (uff58.getUffBasedFRF() == null || !uff58.getUffBasedFRF().equals(this)) {
//            uff58.setUffBasedFRF(this);
//        }
    }
}
