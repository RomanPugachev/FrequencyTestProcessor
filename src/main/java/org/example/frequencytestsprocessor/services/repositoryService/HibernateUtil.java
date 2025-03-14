package org.example.frequencytestsprocessor.services.repositoryService;

import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactoryInstance;

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .configure("hibernate.properties")
                    .addPackage("org.example.frequencytestsprocessor.datamodel.databaseModel");
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
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
