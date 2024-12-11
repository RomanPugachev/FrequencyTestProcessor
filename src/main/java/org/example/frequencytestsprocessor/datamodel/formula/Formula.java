package org.example.frequencytestsprocessor.datamodel.formula;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

@Getter
@Setter
public abstract class Formula implements IdManager.HasId{
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
    public Formula(String formulaString, String comment) {
        this.formulaString = formulaString;
        this.comment = comment;
        this.formulaType = FormulaType.UNKNOWN;
    }
    public Formula(String formulaString, String comment, FormulaType formulaType) {
        this.formulaString = formulaString;
        this.comment = comment;
        this.formulaType = formulaType;
    }
    public abstract boolean validate(String formulaString);

}
