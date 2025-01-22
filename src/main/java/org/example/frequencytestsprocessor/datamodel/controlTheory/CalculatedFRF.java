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

    private CalculatedFRF(){
        this.frequencies = new ArrayList<>();
        this.complexValues = new ArrayList<>();
    }

    public static CalculatedFRF ofFreqsAndComplex(List<Double> frequencies, List<Complex> complexValues) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frequencies);
        result.setComplexValues(complexValues);
        return result;
    }

    public static CalculatedFRF additionResult(FRF frf1, FRF frf2) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.additionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF additionResult(FRF frf, Complex valueForAdding) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        for (int i = 0; i < frf.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.additionResult(frf.getComplexValues().get(i), valueForAdding));
        }
        return result;
    }

    public static CalculatedFRF additionResult(Complex valueForAdding, FRF frf) {
        return additionResult(frf, valueForAdding);
    }

    public static CalculatedFRF additionResult(FRF frf, Double valueForAdding) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        for (int i = 0; i < frf.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.additionResult(frf.getComplexValues().get(i), valueForAdding));
        }
        return result;
    }

    public static CalculatedFRF additionResult(Double valueForAdding, FRF frf) {
        return additionResult(frf, valueForAdding);
    }

    public static CalculatedFRF subtractionResult(FRF frf1, FRF frf2){
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.subtractionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF subtractionResult(FRF frf, Complex valueForExtracting) {
        return subtractionResult(frf, valueForExtracting, true);
    }

    public static CalculatedFRF subtractionResult(Complex valueForExtracting, FRF frf) {
        return subtractionResult(frf, valueForExtracting, false);
    }

    public static CalculatedFRF subtractionResult(FRF frf, Double valueForExtracting) {
        return subtractionResult(frf, valueForExtracting, true);
    }

    public static CalculatedFRF subtractionResult(Double valueForExtracting, FRF frf) {
        return subtractionResult(frf, valueForExtracting, false);
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

    public static CalculatedFRF multiplicationResult(FRF frf, Complex valueForMultiplying) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        for (int i = 0; i < frf.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.multiplicationResult(frf.getComplexValues().get(i), valueForMultiplying));
        }
        return result;
    }

    public static CalculatedFRF multiplicationResult(Complex valueForMultiplying, FRF frf) {
        return multiplicationResult(frf, valueForMultiplying);
    }

    public static CalculatedFRF divisionResult(FRF frf1, FRF frf2){
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf1.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf1.getFrequencies().size());
        for (int i = 0; i < frf1.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.divisionResult(frf1.getComplexValues().get(i),frf2.getComplexValues().get(i)));
        }
        return result;
    }

    public static CalculatedFRF divisionResult(FRF frf1, Complex valueForDividing) {
        return divisionResult(frf1, valueForDividing, true);
    }

    public static CalculatedFRF divisionResult(Complex valueForDividing, FRF frf1) {
        return divisionResult(frf1, valueForDividing, false);
    }

    public static CalculatedFRF poweringResult(FRF frf, Double power) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        for (int i = 0; i < frf.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.poweringResult(frf.getComplexValues().get(i), power));
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

    private static CalculatedFRF subtractionResult(FRF frf, Complex valueForExtracting, boolean isFirst) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        if (isFirst) {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.subtractionResult(frf.getComplexValues().get(i), valueForExtracting));
            }
        } else {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.subtractionResult(valueForExtracting, frf.getComplexValues().get(i)));
            }
        }
        return result;
    }

    private static CalculatedFRF subtractionResult(FRF frf, Double valueForExtracting, boolean isFirst) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        if (isFirst) {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.subtractionResult(frf.getComplexValues().get(i), valueForExtracting));
            }
        } else {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.subtractionResult(valueForExtracting, frf.getComplexValues().get(i)));
            }
        }
        return result;
    }

    public static CalculatedFRF divisionResult(FRF frf, Complex valueForDividing, boolean isFirst) {
        CalculatedFRF result = new CalculatedFRF();
        result.setFrequencies(frf.getFrequencies());
        List<Complex> resultComplexValues = new ArrayList<>(frf.getFrequencies().size());
        if (isFirst) {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.divisionResult(frf.getComplexValues().get(i), valueForDividing));
            }
        } else {
            for (int i = 0; i < frf.getFrequencies().size(); i++) {
                resultComplexValues.add(Complex.divisionResult(valueForDividing, frf.getComplexValues().get(i)));
            }
        }
        return result;
    }

}
