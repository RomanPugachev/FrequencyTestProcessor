package org.example.frequencytestsprocessor.services;

import lombok.Getter;
import lombok.Setter;

import java.util.ResourceBundle;

import lombok.Getter;

import java.io.*;
import java.util.Properties;
@Getter
public class PropertyService {
    private Properties properties;

    public PropertyService(String pathToFile) {
        this.properties = loadProperties(pathToFile);
    }

    public Properties loadProperties(String pathToFile) {
        Properties properties = new Properties();
        try(InputStream is = PropertyService.class.getResourceAsStream(pathToFile)){
            properties.load(is);
        } catch (IOException e) {
            System.out.println("Не удалось считать файл configuration.properties");
            return null;
        }
        return properties;
    }
}
/*
mainApplicationName.ru = Обработчик частотных испытаний
mainApplicationName.en = Frequency tests processor
mainMenuBar.file.ru =Файл
mainMenuBar.file.en=File
mainMenuBar.file.close.ru =Закрыть
mainMenuBar.file.close.en=Close
mainMenuBar.Settings.ru =Настройки
mainMenuBar.Settings.en=Settings
mainMenuBar.Settings.languageSettings.ru =Настройки языка
mainMenuBar.Settings.languageSettings.en=Language settings
mainMenuBar.Settings.languageSettings.language_ru.ru =Русский
mainMenuBar.Settings.languageSettings.language_ru.en=Русский
mainMenuBar.Settings.languageSettings.language_en.ru =English
mainMenuBar.Settings.languageSettings.language_en.en=English
*/