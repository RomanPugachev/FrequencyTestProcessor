package org.example.frequencytestsprocessor.datamodel.formula;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.datasetRepresentation.RepresentableDataset;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

@Getter
public class Formula implements IdManager.HasId{
    protected String formulaString;
    @Setter
    @JsonProperty("idForTable")
    protected String id;
    @Setter
    protected String comment;
    @Setter
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


    public boolean validate(String formulaString){
        System.out.println("DEFAULT VALIDATION");
        return false;
    }

    public void setFormulaString(String formulaString) {
        System.out.println("DEFAULT SET FORMULA STRING");
    }
}