package org.example.frequencytestsprocessor.converters;

import jakarta.persistence.AttributeConverter;

import java.util.List;

import static org.example.frequencytestsprocessor.commons.CommonMethods.*;

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
