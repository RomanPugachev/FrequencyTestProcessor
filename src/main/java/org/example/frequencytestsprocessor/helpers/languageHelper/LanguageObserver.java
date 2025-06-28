package org.example.frequencytestsprocessor.helpers.languageHelper;

import java.util.Properties;

public interface LanguageObserver {
    void updateLanguage(Properties languageProperties, String currentLanguage, String previousLanguage);
}
