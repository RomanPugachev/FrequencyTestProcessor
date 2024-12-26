package org.example.frequencytestsprocessor.datamodel.formula;

import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;

public class AnalyticalFormula extends Formula{
    public AnalyticalFormula() {
        super("1", "This is test analytical formula", FormulaType.ANALYTICAL);
    }

    public void setFormulaString(String formulaString) {
        this.formulaString = formulaString;
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }

    public RepresentableDataset getDataset(Long runNumber) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
