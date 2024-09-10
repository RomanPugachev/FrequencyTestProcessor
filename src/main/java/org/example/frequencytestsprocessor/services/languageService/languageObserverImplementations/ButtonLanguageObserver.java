package org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.util.Properties;
import java.nio.charset.StandardCharsets;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;

@AllArgsConstructor
public class ButtonLanguageObserver implements LanguageObserver {
    private Button button;
    @Override
    public void updateLanguage(Properties languageProperties, String currentLanguage) {
        String key = button.getId() + DOT;
        String text = languageProperties.getProperty(key + currentLanguage);
        if (text != null) {
            byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            button.setText(decodedText);
        }
    }
}

//File
//Settings
//Language settings
//Файл
//Настройки
//Настройки языка

//File
//Settings
//Language settings
//файл
//Настройки
//Настройки языка
