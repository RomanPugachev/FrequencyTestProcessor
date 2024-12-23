package org.example.frequencytestsprocessor.datamodel.formula;

import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.ArrayList;
import java.util.List;

public class SensorBasedFormula extends Formula {

    private List<String>

    public SensorBasedFormula() {
        super("Some formula string", "This is test formula", FormulaType.SENSOR_BASED);
        updateInformation();
    }

    private void updateInformation() {

    }

    public boolean validate(String formulaString) {
        // TODO: Implement validation logic
        System.out.println("Validating formula: " + formulaString);
        System.out.println("For now I just return true");
        return true;
    }

    public List<String> getDependentIds(){
        List<String> dependentIds = new ArrayList<>();

    }
}
