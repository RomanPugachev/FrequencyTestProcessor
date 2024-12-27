package org.example.frequencytestsprocessor.datamodel.controlTheory;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;


// TODO: IMPLEMENT THIS CLASS
public class FRF {
    @Getter
    @Setter
    private List<Double> frequencies;
    @Getter
    @Setter
    private List<Complex> complexValues;
}
