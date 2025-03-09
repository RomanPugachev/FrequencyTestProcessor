package org.example.frequencytestsprocessor.converters;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleListConverter implements AttributeConverter<List<Double>, String> {
    @Override
    public String convertToDatabaseColumn(List<Double> attribute) {
        return attribute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    @Override
    public List<Double> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(";"))
                .map(Double::valueOf)
                .toList();
    }
}
