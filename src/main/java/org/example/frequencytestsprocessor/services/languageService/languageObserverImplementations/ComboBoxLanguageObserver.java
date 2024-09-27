package org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations;

import javafx.scene.control.ComboBox;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;

public class ComboBoxLanguageObserver implements LanguageObserver {
    private ComboBox<?> comboBox;

    public ComboBoxLanguageObserver(ComboBox<?> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public void updateLanguage(Properties languageProperties, String currentLanguage) {
        String key = comboBox.getId() + DOT;
        String translatedText = languageProperties.getProperty(key + currentLanguage);
        if (translatedText != null) {
            byte[] bytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            comboBox.setPromptText(decodedText);
        }
    }
}
