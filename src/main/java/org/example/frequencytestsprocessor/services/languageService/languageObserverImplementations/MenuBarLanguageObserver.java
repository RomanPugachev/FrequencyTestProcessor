package org.example.frequencytestsprocessor.services.languageService.languageObserverImplementations;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.example.frequencytestsprocessor.services.languageService.LanguageObserver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;
import static org.example.frequencytestsprocessor.commons.StaticStrings.MAIN_MENU_BAR;

public class MenuBarLanguageObserver implements LanguageObserver {
    private MenuBar menuBar;

    public MenuBarLanguageObserver(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    @Override
    public void updateLanguage(Properties languageProperties, String newLanguage) {
        for (Menu menu : menuBar.getMenus()) {
            updateMenu(menu, languageProperties, newLanguage, MAIN_MENU_BAR + DOT);
        }
    }

    private void updateMenu(Menu menu, Properties languageProperties, String newLanguage, String previousKey) {
        String key = previousKey + menu.getId().toString() + DOT;
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
        String key = previousKey + menuItem.getId().toString() + DOT;
        String translatedText = languageProperties.getProperty(key + newLanguage);
        if (translatedText != null) {
            byte[] bytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
            String decodedText = new String(bytes, StandardCharsets.UTF_8);
            menuItem.setText(decodedText);
        }
    }
}