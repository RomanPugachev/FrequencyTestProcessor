package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;


import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.converters.ComplexListConverter;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

@Embeddable
@Getter
@Setter
public class RawFrequencyData {
    @Convert(converter = DoubleListConverter.class)
    private List<Double> frequencies;
    @Convert(converter = ComplexListConverter.class)
    private List<Complex> complexValues;

    public RawFrequencyData() {
    }

    public RawFrequencyData(String frequencies, String complexValues) {
        this.frequencies = CommonMethods.convertStringToListOfDouble(frequencies);
        this.complexValues = CommonMethods.convertStringToComplexList(complexValues);
    }

    public RawFrequencyData(List<Double> frequencies, List<Complex> complexValues) {
        this.frequencies = frequencies;
        this.complexValues = complexValues;
    }
}
