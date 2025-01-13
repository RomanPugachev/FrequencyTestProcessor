package org.example.frequencytestsprocessor.datamodel.controlTheory;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CalculatedFRF implements FRF {
    private List<Double> frequencies;
    private List<Complex> complexValues;



    public static CalculatedFRF additionResult(FRF frf1, FRF frf2) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.additionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF extractionResult(FRF frf1, FRF frf2){
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.extractionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF multiplicationResult(FRF frf1, FRF frf2){
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.multiplicationResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF divisionResult(FRF frf1, FRF frf2){
        // TODO: implement this function
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.divisionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    @Override
    public List<Double> getXData() {
        return complexValues.stream().map(Complex::getReal).toList();
    }

    @Override
    public List<Double> getYData() {
        return complexValues.stream().map(Complex::getImag).toList();
    }
}
