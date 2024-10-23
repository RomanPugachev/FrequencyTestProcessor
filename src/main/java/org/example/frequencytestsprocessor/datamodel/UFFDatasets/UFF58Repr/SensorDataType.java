package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;
import lombok.*;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

import static org.example.frequencytestsprocessor.commons.StaticStrings.*;


@AllArgsConstructor
@EqualsAndHashCode(exclude = {"parentSection", "sensors"})
public class SensorDataType {
    public static final SensorDataType DEFAULT_TYPE = new SensorDataType("DEFAULT TYPE");
    public static final LanguageObserver DEFAULT_TYPE_LANGUAGE_OBSERVER = (languageProperties, currentLanguage) -> {
        String key = DEFAULT_TYPE_ID + DOT;
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
    private String typeName;
    @Getter
    private Set<Sensor> sensors;
    @Getter
    @Setter
    Section parentSection;
    public SensorDataType(String typeName) {
        this.typeName = typeName;
    }
    public SensorDataType addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.parentType = this;
        return this;
    }
}