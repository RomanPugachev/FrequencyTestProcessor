package org.example.frequencytestsprocessor.services.databaseInteractions;

import org.example.frequencytestsprocessor.controllers.MainController;
import org.hibernate.SessionFactory;

public class FRFRepository {
    private MainController mainController;

    private SessionFactory sessionFactory;

    private static FRFRepository frfRepositoryInstace;

    public FRFRepository getRepository() {
        if (frfRepositoryInstace == null) {
            frfRepositoryInstace = new FRFRepository();
        }
        return frfRepositoryInstace;
    }

    public void saveFRF(String frf) {
        // Implement the logic to save the FRF to the database
    }

    public static void setInstMainController(MainController mainController) {
        frfRepositoryInstace.setMainController(mainController);
    }
    private FRFRepository() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    private void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}