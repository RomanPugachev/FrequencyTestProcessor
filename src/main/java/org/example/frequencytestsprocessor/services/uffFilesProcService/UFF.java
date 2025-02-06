package org.example.frequencytestsprocessor.services.uffFilesProcService;


import com.fasterxml.jackson.databind.ObjectMapper;
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


@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class UFF implements Iterable<UFF58> {
    @Getter
    private List<Long> toBeProcessedDatasetsIndices;
    @Getter
    @Setter
    private List<Long> typesOfDatasets;
    @Getter
    @Setter
    private List<UFFDataset> datasets;

    private UFF() {
        typesOfDatasets = null;
        datasets = null;
    }

    public Iterator<UFF58> iterator() {
        return new Iterator<UFF58>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < toBeProcessedDatasetsIndices.size();
            }

            @Override
            public UFF58 next() {
                return (UFF58) datasets.get(toBeProcessedDatasetsIndices.get(index++).intValue());
            }
        };
    }

    public static UFF readUNVFile(String fileAddress) {
        // Basic initialization
        UFF resultUFF = new UFF();
        ObjectMapper objectMapper = MainController.getObjectMapper();
        //Read data with Python
        byte[] pythonOutput = UFF.getPythonOutputByteArray(pythonizePathToFile(fileAddress, CommonMethods.PathFrom.SYSTEM));
        // Read the Python output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pythonOutput)))){
            // Setting types of datasets
            String line = reader.readLine();
            resultUFF.setTypesOfDatasets(
                    Arrays.stream(line.trim().split(" "))
                    .map(Long::valueOf)
                    .collect(Collectors.toList()));
            resultUFF.datasets = new ArrayList<>(resultUFF.typesOfDatasets.size()); resultUFF.toBeProcessedDatasetsIndices = new ArrayList<>(resultUFF.typesOfDatasets.size());
            // Read and parse JSON data for each type
            for (int datasetTypeId = 0; datasetTypeId < resultUFF.typesOfDatasets.size(); datasetTypeId++) {
                int timeout = 10;
                while (!reader.ready()){
                    timeout--;
                    Thread.sleep(1000);
                    if (timeout == 0) {
                        throw new RuntimeException("Timeout while waiting for BufferedReader output");
                    }
                }
                line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    datasetTypeId--;
                    continue; // Scip empty lines
                }
                Long datasetType = resultUFF.typesOfDatasets.get(datasetTypeId);
                // Parse JSON data
                try {
                    Class<?> uffClass = Class.forName(BASE_UFF_TYPES_CALSS_PATH + datasetType);
                    UFFDataset uffData = (UFFDataset) objectMapper.readValue(line, uffClass);
                    resultUFF.datasets.add(uffData);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unsupported type of dataset: " + datasetType + "\n" + e.getMessage(), e);
                }
                if (datasetType == 58) { resultUFF.toBeProcessedDatasetsIndices.add(Long.valueOf(datasetTypeId)); }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
        }
        return resultUFF;
    }

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
                 .append(TYPES_OF_DATASETS, typesOfDatasets)
                 .append(TO_BE_PROCESSED_DATASETS_INDICES, toBeProcessedDatasetsIndices)
                 .append(DATASETS, datasets)
                 .toString();
    }
}