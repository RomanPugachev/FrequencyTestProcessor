package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public abstract class Formula {
    String content;
    String id;
    String description;

    FormulaType formulaType;
//     To be continued
    public enum FormulaType {
        ANALYTICAL,
        SENSOR_BASED
    }
}
