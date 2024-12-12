package org.example.frequencytestsprocessor.datamodel.UFFDatasets;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

import static org.example.frequencytestsprocessor.controllers.MainController.objectMapper;

public class UFF58 extends UFFDataset {
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

        @SneakyThrows
        @Override
        public String toString(){
                return objectMapper.writeValueAsString(this);
        }
    }