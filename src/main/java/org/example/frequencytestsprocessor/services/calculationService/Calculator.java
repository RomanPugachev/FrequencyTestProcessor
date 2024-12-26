package org.example.frequencytestsprocessor.services.calculationService;

import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private final MainController mainController;

    public List<String> getCalculationIdSequence(List<String> basicIds, TableView<Formula> formulaTable){
        List<String> calculationIdSequence = new ArrayList<>();
        boolean addedNewPossibility = true;
        List<Formula> formulaList = formulaTable.getItems();
        while (addedNewPossibility) {
            addedNewPossibility = false;
        }
    }

    public Calculator(MainController mainController) {
        this.mainController = mainController;
    }

}
