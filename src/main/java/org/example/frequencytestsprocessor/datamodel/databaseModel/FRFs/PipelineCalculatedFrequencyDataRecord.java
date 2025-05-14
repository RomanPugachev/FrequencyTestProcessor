package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.converters.MapEntryConverter;
import org.example.frequencytestsprocessor.converters.SensorBasedFormulaConverter;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFFDataSource;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;

import java.util.List;
import java.util.Map;

/***
 * This class will represent frequency data records that are calculated from following pipeline of calculations:
 *  user chooses section, type and runs, which available in data source user works with.
 * Datasource type must be .uff or .unv. Each dataset of particular section, type and run must share the same frequencies.
 * Values of frequencies and complex values are calculated from formula, which user specifies for calculation.
 */

@Getter
@Setter
@Entity
@Table(name = "pipelineCalculatedFreqencyDataRecords")
@DiscriminatorValue(value="pipelineCalculatedFrequencyDataRecords")
public class PipelineCalculatedFrequencyDataRecord extends CalculatedFrequencyDataRecord {

    @Convert(converter = SensorBasedFormulaConverter.class)
    private Formula formula;

    private String sectionName;

    private String sensorDataTypeName;

//    @Convert(converter = MapEntryConverter.class)
//    private Map.Entry<String, Long> formulaId2RecordId;

    private Long runId;

    @Embedded
    private RawFrequencyData calculatedData;

    // TODO: implement relationship with UFFDataSource
    @ManyToOne
    @JoinColumn(name = "sourceId")
    private UFFDataSource source;



    public PipelineCalculatedFrequencyDataRecord() {
        super();
    }

    @Override
    public void refreshRawFrequencyData() {
        // TODO: implement fetching and calculating raw frequency data from formula and source
        setRawFrequencyData(calculatedData);
    }

    public PipelineCalculatedFrequencyDataRecord(Formula formula, String sectionName, String sensorDataTypeName, Long runId, UFFDataSource source, FRF calculatedFRF) {
        super(formula.getComment());
        this.formula = formula;
        this.sectionName = sectionName;
        this.sensorDataTypeName = sensorDataTypeName;
        this.runId = runId;
        this.source = source;
        calculatedData = new RawFrequencyData(calculatedFRF.getFrequencies(), calculatedFRF.getComplexValues());
        setRawFrequencyData(calculatedData);
    }
}
