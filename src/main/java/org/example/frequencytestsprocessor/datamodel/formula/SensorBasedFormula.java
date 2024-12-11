package org.example.frequencytestsprocessor.datamodel.formula;

import org.example.frequencytestsprocessor.services.idManagement.IdManager;

public class SensorBasedFormula extends Formula {

    public SensorBasedFormula() {
        super("Some formula string", "This is test formula", FormulaType.SENSOR_BASED);
    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }

}
