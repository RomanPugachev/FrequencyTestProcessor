package org.example.frequencytestsprocessor.helpers;

import javafx.scene.control.TableView;
import lombok.Setter;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.formula.AnalyticalFormula;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;

import java.util.*;

public class Calculator {
    private final MainController mainController;
    @Setter
    private TableView<Formula> formulaTable;

    public List<String> getCalculationIdSequence(List<String> basicIds){
        List<String> calculationIdSequence = new ArrayList<>();
        boolean addedNewPossibility = true;
        List<Formula> formulaList = formulaTable.getItems();
        Long numberOfPosbileCalculations = 0L;
        while (addedNewPossibility) {
            addedNewPossibility = false;
            for (Formula formula : formulaList) {
                if (calculationIdSequence.contains(formula.getId())) continue;
                if (formula instanceof AnalyticalFormula) {
                    calculationIdSequence.add(formula.getId());
                    addedNewPossibility = true;
                    numberOfPosbileCalculations++;
                } else if (formula instanceof SensorBasedFormula) {
                    Set<String> depIds = ((SensorBasedFormula) formula).defineDependentIds();
                    if (depIds.stream().allMatch(id -> basicIds.contains(id) || calculationIdSequence.contains(id))) {
                        calculationIdSequence.add(formula.getId());
                        numberOfPosbileCalculations++;
                        addedNewPossibility = true;
                    }
                }
            }
        }
        if (numberOfPosbileCalculations < formulaList.size()) {
            throw new RuntimeException("Can't calculate all formulas");
        }
        return calculationIdSequence;
    }

    public List<Double> getFrequencies(Long runId) {
        List<Sensor> sensors = mainController.getChosenSensorsTable().getItems();
        List<Double> frequencies = sensors.stream().map(s -> s.getData().get(runId).getFrequencies()).findFirst().get();
        sensors.forEach(s -> {
            if (!frequencies.equals(s.getData().get(runId).getFrequencies())) {
                throw new RuntimeException("Frequencies are not equal");
            }
        });
        return new ArrayList<>(frequencies);
    }

    public FRF calculateFRF(Long runId, String currentId, List<Double> frequencies, Map<Long, Set<Map.Entry<String, FRF>>> calculatedFRFs) {
        Formula curentFormula = formulaTable.getItems().stream().filter(formula -> formula.getId().equals(currentId)).findFirst().orElseThrow(() -> new RuntimeException("Cannot find formula with id " + currentId));
        FRF frf = null;
        if (curentFormula instanceof SensorBasedFormula) {
            frf = ((SensorBasedFormula) curentFormula).calculate(runId, mainController.getChosenSensorsTable(), calculatedFRFs);
        } else if (curentFormula instanceof AnalyticalFormula) {
            frf = ((AnalyticalFormula) curentFormula).extractFRFByFrequencies(frequencies);
        } else {
            throw new RuntimeException("Unknown formula type");
        }
        if (frf != null) return frf;
        else throw new RuntimeException("Failed to calculate FRF");
    }

    public Calculator(MainController mainController) {
        this.mainController = mainController;
    }

}
