package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;
import lombok.*;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.example.frequencytestsprocessor.commons.CommonMethods.print;
import static org.example.frequencytestsprocessor.commons.StaticStrings.DEFAULT_SECTION_ID;
import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;

@EqualsAndHashCode(exclude = {"types"})
@NoArgsConstructor
public class Section {
    public static final Section DEFAULT_SECTION = (new Section("DEFAULT SECTION")).addType(SensorDataType.DEFAULT_TYPE);
    public static final LanguageObserver DEFAULT_SECTION_LANGUAGE_OBSERVER = (languageProperties, currentLanguage) -> {
        String key = DEFAULT_SECTION_ID + DOT;
        String text = languageProperties.getProperty(key + currentLanguage);
        if (text != null) {
            byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            Section.DEFAULT_SECTION.setSectionName(decodedText);
        } else {
            throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
        }
    };
    @Getter
    @Setter
    private String sectionName;
    @Getter
    @Setter
    private Set<SensorDataType> types = new HashSet<>();
    public Section addType(SensorDataType type) {
        types.add(type);
        type.parentSection = this;
        return this;
    }
    public Section(String sectionName) {
        this.sectionName = sectionName;
    }

    public Section(String sectionName, Set<SensorDataType> types) {
        this.sectionName = sectionName;
        this.types = types;
        for (SensorDataType type : types) {
            type.parentSection = this;
        }
    }
}