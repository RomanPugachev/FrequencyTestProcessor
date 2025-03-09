package org.example.frequencytestsprocessor.datamodel.UFFDatasets;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.frequencytestsprocessor.converters.ComplexListConverter;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

import static org.example.frequencytestsprocessor.controllers.MainController.objectMapper;

@Entity
@Table(name = "uff58")
@DiscriminatorValue(value = "58")
@Getter
@Setter
public class UFF58 extends UFFDataset {
    private int binary;
    private String id1;
    private String id2;
    private String id3;
    private String id4;
    private String id5;
    @JsonProperty("func_type")
    private int funcType;
    @JsonProperty("func_id")
    private int funcId;
    @JsonProperty("ver_num")
    private int verNum;
    @JsonProperty("load_case_id")
    private int loadCaseId;
    @JsonProperty("rsp_ent_name")
    private String rspEntName;
    @JsonProperty("rsp_node")
    private int rspNode;
    @JsonProperty("rsp_dir")
    private int rspDir;
    @JsonProperty("ref_ent_name")
    private String refEntName;
    @JsonProperty("ref_node")
    private int refNode;
    @JsonProperty("ref_dir")
    private int refDir;
    @JsonProperty("ord_data_type")
    private int ordDataType;
    @JsonProperty("num_pts")
    private int numPts;
    @JsonProperty("abscissa_spacing")
    private int abscissaSpacing;
    @JsonProperty("abscissa_min")
    private double abscissaMin;
    @JsonProperty("abscissa_inc")
    private double abscissaInc;
    @JsonProperty("z_axis_value")
    private double zAxisValue;
    @JsonProperty("abscissa_spec_data_type")
    private int abscissaSpecDataType;
    @JsonProperty("abscissa_len_unit_exp")
    private int abscissaLenUnitExp;
    @JsonProperty("abscissa_force_unit_exp")
    private int abscissaForceUnitExp;
    @JsonProperty("abscissa_temp_unit_exp")
    private int abscissaTempUnitExp;
    @JsonProperty("abscissa_axis_lab")
    private String abscissaAxisLab;
    @JsonProperty("abscissa_axis_units_lab")
    private String abscissaAxisUnitsLab;
    @JsonProperty("ordinate_spec_data_type")
    private int ordinateSpecDataType;
    @JsonProperty("ordinate_len_unit_exp")
    private int ordinateLenUnitExp;
    @JsonProperty("ordinate_force_unit_exp")
    private int ordinateForceUnitExp;
    @JsonProperty("ordinate_temp_unit_exp")
    private int ordinateTempUnitExp;
    @JsonProperty("ordinate_axis_lab")
    private String ordinateAxisLab;
    @JsonProperty("ordinate_axis_units_lab")
    private String ordinateAxisUnitsLab;
    @JsonProperty("orddenom_spec_data_type")
    private int orddenomSpecDataType;
    @JsonProperty("orddenom_len_unit_exp")
    private int orddenomLenUnitExp;
    @JsonProperty("orddenom_force_unit_exp")
    private int orddenomForceUnitExp;
    @JsonProperty("orddenom_temp_unit_exp")
    private int orddenomTempUnitExp;
    @JsonProperty("orddenom_axis_lab")
    private String orddenomAxisLab;
    @JsonProperty("orddenom_axis_units_lab")
    private String orddenomAxisUnitsLab;
    @JsonProperty("z_axis_spec_data_type")
    private int zAxisSpecDataType;
    @JsonProperty("z_axis_len_unit_exp")
    private int zAxisLenUnitExp;
    @JsonProperty("z_axis_force_unit_exp")
    private int zAxisForceUnitExp;
    @JsonProperty("z_axis_temp_unit_exp")
    private int zAxisTempUnitExp;
    @JsonProperty("z_axis_axis_lab")
    private String zAxisAxisLab;
    @JsonProperty("z_axis_axis_units_lab")
    private String zAxisAxisUnitsLab;
    @JsonProperty("x")
    @Convert(converter = DoubleListConverter.class)
    @Column(nullable = false)
    private String frequencies;
    @JsonProperty("data")
    @Convert(converter = ComplexListConverter.class)
    @Column(nullable = false)
    private String complexValues;

    public List<Complex> getComplexValues() {
        return new ComplexListConverter().convertToEntityAttribute(complexValues);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return objectMapper.writeValueAsString(this);
    }
}