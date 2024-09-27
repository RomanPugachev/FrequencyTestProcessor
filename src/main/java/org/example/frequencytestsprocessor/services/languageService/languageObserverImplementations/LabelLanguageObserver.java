package org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations;

import javafx.scene.control.Label;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;

public class LabelLanguageObserver implements LanguageObserver {
    private Label label;

    public LabelLanguageObserver(Label label) {
        this.label = label;
    }

    @Override
    public void updateLanguage(Properties languageProperties, String currentLanguage) {
        String key = label.getId() + DOT;
        if (key.equals("chosenFileLabel.") && label.getText().indexOf(":") >= 0) {
            return;
        }
        String translatedText = languageProperties.getProperty(key + currentLanguage);
        if (translatedText != null) {
            byte[] bytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            label.setText(decodedText);
        }
    }
}
