package org.example.frequencytestsprocessor.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Map;

import static org.example.frequencytestsprocessor.commons.CommonMethods.convertListOfDoubleToString;
import static org.example.frequencytestsprocessor.commons.CommonMethods.convertStringToListOfDouble;

@Converter
public class MapEntryConverter implements AttributeConverter<Map.Entry<String, Long>, String> {
    @Override
    public String convertToDatabaseColumn(Map.Entry<String, Long> attribute) {
        return attribute.getKey() + ":" + attribute.getValue();
    }

    @Override
    public Map.Entry<String, Long> convertToEntityAttribute(String dbData) {
        return Map.entry(dbData.split(":")[0], Long.parseLong(dbData.split(":")[1]));
    }
}