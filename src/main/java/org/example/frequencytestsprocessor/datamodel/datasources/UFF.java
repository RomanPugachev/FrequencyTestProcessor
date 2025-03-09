package org.example.frequencytestsprocessor.datamodel.datasources;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jep.Jep;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFFDataset;
import org.example.frequencytestsprocessor.services.PythonInterpreterService;


import static org.example.frequencytestsprocessor.commons.CommonMethods.pythonizePathToFile;
import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "uffSources")
@EqualsAndHashCode
@AllArgsConstructor
@DiscriminatorValue("UFF")
public class UFF extends DataSource {
    @Getter
    @Setter
    @OneToMany(orphanRemoval = true)
    private List<UFFDataset> datasets;

    public UFF() {super();}

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
                 .append("source_file", getSourceName())
                 .append(DATASETS, datasets)
                 .toString();
    }
}