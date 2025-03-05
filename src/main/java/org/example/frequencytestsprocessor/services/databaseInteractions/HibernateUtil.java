package org.example.frequencytestsprocessor.services.databaseInteractions;

import org.example.frequencytestsprocessor.datamodel.datasources.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactoryinstance;

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.properties") // Loads hibernate.properties from classpath
                    .build();
            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClass(DataSource.class)
                    .addAnnotatedClass();
            return sources.buildMetadata().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactoryinstance == null) {
            sessionFactoryinstance = buildSessionFactory();
        }
        return sessionFactoryinstance;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
