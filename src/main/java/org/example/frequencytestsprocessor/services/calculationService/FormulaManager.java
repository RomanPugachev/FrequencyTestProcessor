package org.example.frequencytestsprocessor.services.calculationService;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.ArrayList;
import java.util.List;

public class FormulaManager {
    private final MainController mainController;

    public FormulaManager(MainController mc) {
        mainController = mc;
    }

    public List<String> getCalculationIdSequence(List<String> basicIds, TableView<Formula> formulaTable){
        List<String> calculationIdSequence = new ArrayList<>();
        boolean addedNewPossibility = true;
        List<Formula> formulaList = formulaTable.getItems();
        while (addedNewPossibility) {
            addedNewPossibility = false;
            formulaList.stream().
        }
    }
}
