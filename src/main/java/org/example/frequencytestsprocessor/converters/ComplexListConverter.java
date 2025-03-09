package org.example.frequencytestsprocessor.converters;

import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Converter
public class ComplexListConverter implements AttributeConverter<List<Complex>, String> {

    @Override
    public String convertToDatabaseColumn(List<Complex> complexList) {
        if (complexList == null || complexList.isEmpty()) return "";
        return complexList.stream()
                .map(Complex::toString) // Convert each Complex to string
                .collect(Collectors.joining(";")); // Join using semicolon
    }

    @Override
    public List<Complex> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return List.of();
        return Arrays.stream(dbData.split(";"))
                .map(Complex::fromString) // Convert each string to Complex
                .collect(Collectors.toList());
    }
}