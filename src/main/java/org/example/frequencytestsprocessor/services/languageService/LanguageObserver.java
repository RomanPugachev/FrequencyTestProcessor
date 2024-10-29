package org.example.frequencytestsprocessor.services.languageService;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public interface LanguageObserver {
    void updateLanguage(Properties languageProperties, String currentLanguage);
}
