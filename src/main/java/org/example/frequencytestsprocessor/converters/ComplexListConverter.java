package org.example.frequencytestsprocessor.converters;

import org.example.frequencytestsprocessor.commons.CommonMethods;
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
        return CommonMethods.convertComplexListToString(complexList);
    }

    @Override
    public List<Complex> convertToEntityAttribute(String dbData) {
        return CommonMethods.convertStringToComplexList(dbData);
    }
}