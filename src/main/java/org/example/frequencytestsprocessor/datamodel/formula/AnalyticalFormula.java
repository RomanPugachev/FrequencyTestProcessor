package org.example.frequencytestsprocessor.datamodel.formula;

public class AnalyticalFormula extends Formula{
    public AnalyticalFormula() {
        super("Some formula string", "This is test analytical formula", FormulaType.ANALYTICAL);
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }
}
