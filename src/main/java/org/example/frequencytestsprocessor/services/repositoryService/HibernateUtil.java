package org.example.frequencytestsprocessor.services.repositoryService;

import org.example.frequencytestsprocessor.MainApplication;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactoryInstance;

    private static SessionFactory buildSessionFactory() {
        try {
            // Loading properties from hibernate.properties file
            Properties properties = new Properties();
            var propertiesURL = MainApplication.class.getResource("hibernate.properties");
            if (propertiesURL != null) {
                properties.load(propertiesURL.openStream());
            } else {
                throw new RuntimeException("Unable to find hibernate.properties");
            }

            // Creating the SessionFactory from properties and created classes
            Configuration configuration = new Configuration()
                    .setProperties(properties)
                    .addPackage("org.example.frequencytestsprocessor.datamodel.databaseModel");
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initialization SessionFactory creation failed:\n" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    public static SessionFactory getSessionFactory() {
        if (sessionFactoryInstance == null) {
            sessionFactoryInstance = buildSessionFactory();
        }
        return sessionFactoryInstance;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
