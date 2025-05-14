package org.example.frequencytestsprocessor.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.convertListOfDoubleToString;
import static org.example.frequencytestsprocessor.commons.CommonMethods.convertStringToListOfDouble;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {
    @Override
    public String convertToDatabaseColumn(List<Double> attribute) {
        return convertListOfDoubleToString(attribute);
    }

    @Override
    public List<Double> convertToEntityAttribute(String dbData) {
        return convertStringToListOfDouble(dbData);
    }
}
