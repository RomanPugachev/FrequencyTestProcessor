package org.example.frequencytestsprocessor.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.formula.SensorBasedFormula;

import java.util.Map;

@Converter
public class SensorBasedFormulaConverter implements AttributeConverter<Formula, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(Formula attribute) {
        String json;
        try {
            json = objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    @Override
    public Formula convertToEntityAttribute(String dbData) {
        Formula formula;
        try {
            formula = objectMapper.readValue(dbData, Formula.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return formula;
    }
}