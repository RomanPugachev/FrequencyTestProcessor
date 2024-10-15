package org.example.frequencytestsprocessor.widgetsDecoration;

import javafx.scene.control.*;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;

public class LanguageObserverDecorator<T extends Control> extends WidgetDecorator<T> implements LanguageObserver {
    public LanguageObserverDecorator(T widget) {
        super(widget);
    }

    @Override
    public void updateLanguage(Properties languageProperties, String currentLanguage) {
        String key = widget.getId() + DOT;
        String text = languageProperties.getProperty(key + currentLanguage);
        switch (widget) {
            case Button button -> {
                if (text != null) {
                    byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                    String decodedText = new String(bytes, StandardCharsets.UTF_8);
                    ((Button) widget).setText(decodedText);
                } else {
                    throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                } break;
            }
            case ComboBox comboBox -> {
                if (text != null) {
                    byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                    String decodedText = new String(bytes, StandardCharsets.UTF_8);
                    ((ComboBox<?>) widget).setPromptText(decodedText);
                } else {
                    throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                } break;
            }
            case Label label -> {
                if (text != null) {
                    if (key.equals("chosenFileLabel.") && ((Label) widget).getText().contains(":")) {
                        return; // Check if file path label is not empty, then it mustn't be changed
                    }
                    byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                    String decodedText = new String(bytes, StandardCharsets.UTF_8);
                    ((Label) widget).setText(decodedText);
                } else {
                    throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
                } break;
            }
            case MenuBar menuBar -> {
                for (Menu menu : menuBar.getMenus()) {
                    updateMenu(menu, languageProperties, currentLanguage, key);
                }
            }
            case null, default -> throw new RuntimeException("This method is not implemented yet.");
        }
    }

    private void updateMenu(Menu menu, Properties languageProperties, String newLanguage, String previousKey) {
        String key = previousKey + menu.getId() + DOT;
        String translatedText = languageProperties.getProperty(key + newLanguage);
        if (translatedText != null) {
            byte[] bytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            menu.setText(decodedText);
        }

        for (MenuItem menuItem : menu.getItems()) {
            if (menuItem instanceof Menu) {
                updateMenu((Menu) menuItem, languageProperties, newLanguage, key);
            } else {
                updateMenuItemText(menuItem, languageProperties, newLanguage, key);
            }
        }
    }

    private void updateMenuItemText(MenuItem menuItem, Properties languageProperties, String newLanguage, String previousKey) {
        String key = previousKey + menuItem.getId() + DOT;
        String translatedText = languageProperties.getProperty(key + newLanguage);
        if (translatedText != null) {
            byte[] bytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            menuItem.setText(decodedText);
        }
    }
}
