package org.example.frequencytestsprocessor.services.uffFilesProcService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.example.frequencytestsprocessor.MainController;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class UFF {
    @Getter
    @Setter
    private List<Integer> typesOfDatasets;
    @Getter
    @Setter
    private List<Object> datasets;

    private UFF() {
        typesOfDatasets = null;
        datasets = null;
    }

    public static UFF parseJSON(String json) {
        ObjectMapper objectMapper = MainController.getObjectMapper();
        try {
            return objectMapper.readValue(json, UFF.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return UFF.class.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create UFF instance", e);
        }
    }

    public class UFF151 {
        public int type;
        @JsonProperty("model_name")
        public String modelName;
        public String description;
        @JsonProperty("db_app")
        public String dbApp;
        @JsonProperty("date_db_created")
        public String dateDbCreated;
        @JsonProperty("time_db_created")
        public String timeDbCreated;
        @JsonProperty("version_db1")
        public int versionDb1;
        @JsonProperty("version_db2")
        public int versionDb2;
        @JsonProperty("file_type")
        public int fileType;
        @JsonProperty("date_db_saved")
        public String dateDbSaved;
        @JsonProperty("time_db_saved")
        public String timeDbSaved;
        public String program;
        @JsonProperty("date_file_written")
        public String dateFileWritten;
        @JsonProperty("time_file_written")
        public String timeFileWritten;
    }

    public class UFF164{
        public int type;
        @JsonProperty("units_code")
        public int unitsCode;
        @JsonProperty("units_description")
        public String unitsDescription;
        @JsonProperty("temp_mode")
        public int tempMode;
        public double length;
        public double force;
        public double temp;
        @JsonProperty("temp_offset")
        public double tempOffset;
    }

    public class UFF58 {
        public int type;
        public int binary;
        public String id1;
        public String id2;
        public String id3;
        public String id4;
        public String id5;
        @JsonProperty("func_type")
        public int funcType;
        @JsonProperty("func_id")
        public int funcId;
        @JsonProperty("ver_num")
        public int verNum;
        @JsonProperty("load_case_id")
        public int loadCaseId;
        @JsonProperty("rsp_ent_name")
        public String rspEntName;
        @JsonProperty("rsp_node")
        public int rspNode;
        @JsonProperty("rsp_dir")
        public int rspDir;
        @JsonProperty("ref_ent_name")
        public String refEntName;
        @JsonProperty("ref_node")
        public int refNode;
        @JsonProperty("ref_dir")
        public int refDir;
        @JsonProperty("ord_data_type")
        public int ordDataType;
        @JsonProperty("num_pts")
        public int numPts;
        @JsonProperty("abscissa_spacing")
        public int abscissaSpacing;
        @JsonProperty("abscissa_min")
        public double abscissaMin;
        @JsonProperty("abscissa_inc")
        public double abscissaInc;
        @JsonProperty("z_axis_value")
        public double zAxisValue;
        @JsonProperty("abscissa_spec_data_type")
        public int abscissaSpecDataType;
        @JsonProperty("abscissa_len_unit_exp")
        public int abscissaLenUnitExp;
        @JsonProperty("abscissa_force_unit_exp")
        public int abscissaForceUnitExp;
        @JsonProperty("abscissa_temp_unit_exp")
        public int abscissaTempUnitExp;
        @JsonProperty("abscissa_axis_lab")
        public String abscissaAxisLab;
        @JsonProperty("abscissa_axis_units_lab")
        public String abscissaAxisUnitsLab;
        @JsonProperty("ordinate_spec_data_type")
        public int ordinateSpecDataType;
        @JsonProperty("ordinate_len_unit_exp")
        public int ordinateLenUnitExp;
        @JsonProperty("ordinate_force_unit_exp")
        public int ordinateForceUnitExp;
        @JsonProperty("ordinate_temp_unit_exp")
        public int ordinateTempUnitExp;
        @JsonProperty("ordinate_axis_lab")
        public String ordinateAxisLab;
        @JsonProperty("ordinate_axis_units_lab")
        public String ordinateAxisUnitsLab;
        @JsonProperty("orddenom_spec_data_type")
        public int orddenomSpecDataType;
        @JsonProperty("orddenom_len_unit_exp")
        public int orddenomLenUnitExp;
        @JsonProperty("orddenom_force_unit_exp")
        public int orddenomForceUnitExp;
        @JsonProperty("orddenom_temp_unit_exp")
        public int orddenomTempUnitExp;
        @JsonProperty("orddenom_axis_lab")
        public String orddenomAxisLab;
        @JsonProperty("orddenom_axis_units_lab")
        public String orddenomAxisUnitsLab;
        @JsonProperty("z_axis_spec_data_type")
        public int zAxisSpecDataType;
        @JsonProperty("z_axis_len_unit_exp")
        public int zAxisLenUnitExp;
        @JsonProperty("z_axis_force_unit_exp")
        public int zAxisForceUnitExp;
        @JsonProperty("z_axis_temp_unit_exp")
        public int zAxisTempUnitExp;
        @JsonProperty("z_axis_axis_lab")
        public String zAxisAxisLab;
        @JsonProperty("z_axis_axis_units_lab")
        public String zAxisAxisUnitsLab;
        public List<Double> x;
        public List<Complex> data;
    }

    public static UFF readUNVFile(String fileAddress, ObjectMapper objectMapper) {
        UFF resultUFF = new UFF();
        List<Object> uffDataList = new ArrayList<>();
        Process process = null;
        try {
            process = new ProcessBuilder("python", "UFFReaderApp.py", fileAddress).start();
        } catch (IOException e) {
            if (process != null) {
                process.destroy();
            }
            throw new RuntimeException("Failed to start UFFReaderApp.py process", e);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            // Read types
            List<Integer> types = Arrays.stream(reader.readLine().trim().split(" "))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
            resultUFF.setTypesOfDatasets(types);

            // Read and parse JSON data for each type
            for (Integer type : resultUFF.getTypesOfDatasets()) {
                StringBuilder jsonOutput = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !line.equals("END_OF_JSON")) {
                    jsonOutput.append(line);
                }

                // Parse JSON data
                try {
                    Class<?> uffClass = Class.forName("org.example.frequencytestsprocessor.services.uffFilesProcService.UFF" + type);
                    Object uffData = objectMapper.readValue(jsonOutput.toString(), uffClass);
                    uffDataList.add(uffData);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unsuppoeted type of dataset: " + type + "\n" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
        }
        resultUFF.setDatasets(uffDataList);
        System.out.println("UFF: " + resultUFF);
        return resultUFF;
    }

}


