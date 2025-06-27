package org.example.frequencytestsprocessor.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {
    private Properties properties;

    public PropertyProvider(String pathToFile) {
        this.properties = loadProperties(pathToFile);
    }

    public Properties loadProperties(String pathToFile) {
        Properties properties = new Properties();
        try (InputStream is = PropertyProvider.class.getResourceAsStream(pathToFile)) {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("Couldn't read file by address " + pathToFile);
            return null;
        }
        return properties;
    }

    public Properties getProperties() {
        return this.properties;
    }
}