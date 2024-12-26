package org.example.frequencytestsprocessor.services.calculationService;

import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.datamodel.formula.AnalyticalFormula;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;

import java.util.ArrayList;
import java.util.List;

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
                if (formula instanceof AnalyticalFormula) {
                    basicIds.add(formula.getId());
                    addedNewPossibility = true;
                    numberOfPosbileCalculations++;
                    continue;
                } else if (formula instanceof SensorBasedFormula && ((SensorBasedFormula)formula).getDependentIds().stream().allMatch(basicIds::contains)) {
                    calculationIdSequence.add(formula.getId());
                    numberOfPosbileCalculations++;
                    addedNewPossibility = true;
                }
            }
        }
        if (numberOfPosbileCalculations < formulaList.size()) {
            throw new RuntimeException("Can't calculate all formulas");
        }
        return calculationIdSequence;
    }

    public RepresentableDataset calculate(Long runId, String currentId) {
        SensorBasedFormula formula = (SensorBasedFormula) formulaTable.getItems().stream().filter(f -> f.getId().equals(currentId)).findFirst().get();
        try {
            RepresentableDataset dataset = formula.getDataset();
            return dataset;
        } catch (Exception e) {
            return new RepresentableDataset() {
                @Override
                public String getDatasetId() {
                    return super.getDatasetId();
                }
            };
        }


    }

    public Calculator(MainController mainController) {
        this.mainController = mainController;
    }



}
