package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;


import jakarta.persistence.*;
import jep.Jep;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFFDataset;
import org.example.frequencytestsprocessor.services.PythonInterpreterService;


import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

import java.io.*;
import java.util.List;

@Entity
@Table(name = "uffSources")
@EqualsAndHashCode
@AllArgsConstructor
@DiscriminatorValue("UFF")
public class UFFDataSource extends DataSource {
    @Getter
    @Setter
    @JoinColumn(foreignKey = @ForeignKey(name = "datasetId"))
    @OneToMany(mappedBy = "parentUFF", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UFFDataset> datasets;

    public UFFDataSource() {super();}
    public UFFDataSource(String sourceAddress) {super(sourceAddress);}

    protected static byte[] getPythonOutputByteArray(String UFFPath) {
        Jep pythonInterpreter = PythonInterpreterService.getPythonInterpreter();
        ByteArrayOutputStream pythonOutput = PythonInterpreterService.getPythonOutputStream();
        String pythonScript = CommonMethods.getTextFileContent(PATH_OF_PYTHON_SCRIPT_FOR_UFF);
        pythonInterpreter.exec(pythonScript);
        pythonInterpreter.exec(String.format("parse_UFF('%s')", UFFPath));
        return pythonOutput.toByteArray();
    }
    @Override
    public String toString() {
         return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                 .append("source_file", getSourceAddress())
                 .append(DATASETS, datasets)
                 .toString();
    }
}