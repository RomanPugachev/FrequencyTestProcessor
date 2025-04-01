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
@Setter
@Entity
@Table(name = "uffBasedFRFs")
@DiscriminatorValue(value = "UFFBasedFRF")
public class UFFBasedFRF extends FrequencyDataRecord {
    @OneToOne(mappedBy = "uffBasedFRF")
    private UFF58 uff58;

    @Override
    public void refreshRawFrequencyData() {
        setRawFrequencyData(new RawFrequencyData(uff58.getFrequencies(), uff58.getComplexValues()));
    }
}
