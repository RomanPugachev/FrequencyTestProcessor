package org.example.frequencytestsprocessor.helpers.languageHelper;

import lombok.Getter;
import org.example.frequencytestsprocessor.helpers.PropertyProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

public class LanguageNotifier {
    private String currentLanguage = RU;
    private String previousLanguage = EN;
    private List<LanguageObserver> observers = new ArrayList<>();
    @Getter
    private PropertyProvider lanaguagePropertyProvider;

    public LanguageNotifier (String pathToLanguages) {
        lanaguagePropertyProvider = new PropertyProvider(pathToLanguages);
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
            observer.updateLanguage(lanaguagePropertyProvider.getProperties(), currentLanguage, previousLanguage);
        }
    }

    public void changeLanguage(String newLanguage) {
        previousLanguage = currentLanguage;
        currentLanguage = newLanguage;
        notifyObservers();
    }
}
