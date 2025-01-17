package org.example.frequencytestsprocessor.datamodel.controlTheory;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.Canvas2DPrintable;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

public interface FRF extends Canvas2DPrintable {
    List<Double> getFrequencies();
    void setFrequencies(List<Double> frequencies);
    List<Complex> getComplexValues();
    void setComplexValues(List<Complex> complexValues);
}
