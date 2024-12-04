package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Formula {
    protected String formulaString;
    protected String id;
    protected String comment;
    protected FormulaType formulaType;

    public enum FormulaType {
        ANALYTICAL,
        SENSOR_BASED,
        UNKNOWN
    }

    public Formula() {
        formulaString = "";
        id = "";
        comment = "";
        this.formulaType = FormulaType.UNKNOWN;
    }
    public Formula(String formulaString, String id, String comment) {
        this.formulaString = formulaString;
        this.id = id;
        this.comment = comment;
        this.formulaType = FormulaType.UNKNOWN;
    }
    public Formula(String formulaString, String id, String comment, FormulaType formulaType) {
        this.formulaString = formulaString;
        this.id = id;
        this.comment = comment;
        this.formulaType = formulaType;
    }

}
