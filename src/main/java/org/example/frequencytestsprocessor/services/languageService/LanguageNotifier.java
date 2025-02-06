package org.example.frequencytestsprocessor.services.languageService;

import lombok.Getter;
import org.example.frequencytestsprocessor.services.PropertyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.frequencytestsprocessor.commons.StaticStrings.PATH_TO_LANGUAGES;
import static org.example.frequencytestsprocessor.commons.StaticStrings.RU;

public class LanguageNotifier {
    private String currentLanguage = RU;
    private List<LanguageObserver> observers = new ArrayList<>();
    @Getter
    private PropertyService lanaguagePropertyService;

    public LanguageNotifier (String pathToLanguages) {
        lanaguagePropertyService = new PropertyService(pathToLanguages);
    }

    public void addObserver(LanguageObserver observer) {
        observers.add(observer);
    }

    public void addObserver(Collection<LanguageObserver> observers) {
        this.observers.addAll(observers);
    }

    public void removeObserver(LanguageObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (LanguageObserver observer : observers) {
            observer.updateLanguage(lanaguagePropertyService.getProperties(), currentLanguage);
        }
    }

    public void changeLanguage(String newLanguage) {
        currentLanguage = newLanguage;
        notifyObservers();
    }
}
