package org.example.frequencytestsprocessor.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

import static org.example.frequencytestsprocessor.commons.CommonMethods.*;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String>{
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return convertListOfLongToString(attribute);
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return convertStringToListOfLong(dbData);
    }
}
