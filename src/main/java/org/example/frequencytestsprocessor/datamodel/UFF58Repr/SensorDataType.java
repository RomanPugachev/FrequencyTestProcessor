package org.example.frequencytestsprocessor.datamodel.UFF58Repr;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.helpers.languageHelper.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DEFAULT_TYPE_ID;
import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;


@AllArgsConstructor
@EqualsAndHashCode(exclude = {"parentSection", "sensors"})
public class SensorDataType {
    public static final SensorDataType DEFAULT_TYPE = new SensorDataType("DEFAULT TYPE");
    public static final LanguageObserver DEFAULT_TYPE_LANGUAGE_OBSERVER = (languageProperties, currentLanguage, previousLanguage) -> {
        String key = DEFAULT_TYPE_ID + DOT;
        String text = languageProperties.getProperty(key + currentLanguage);
        if (text != null) {
            byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            SensorDataType.DEFAULT_TYPE.setTypeName(decodedText);
        } else {
            throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
        }
    };
    @Getter
    @Setter
    private String typeName;
    @Getter
    private Set<Sensor> sensors = new HashSet<>();
    @Getter
    @Setter
    Section parentSection;

    public SensorDataType(String typeName) {
        this.typeName = typeName;
    }

    public SensorDataType addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.setParentType(this);
        return this;
    }

    public String toString() {
        return this.typeName;
    }
}