package org.example.frequencytestsprocessor.services.repositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Transaction;
import jep.Jep;
import org.example.frequencytestsprocessor.commons.CommonMethods;
import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFFDataset;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFFDataSource;
import org.example.frequencytestsprocessor.services.PythonInterpreterService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.pythonizePathToFile;
import static org.example.frequencytestsprocessor.commons.StaticStrings.BASE_UFF_TYPES_CALSS_PATH;
import static org.example.frequencytestsprocessor.commons.StaticStrings.PATH_OF_PYTHON_SCRIPT_FOR_UFF;

public class FRFRepository {
    private static FRFRepository frfRepositoryInstace;

    private MainController mainController;

    private SessionFactory sessionFactory;

    public static FRFRepository getRepository() {
        if (frfRepositoryInstace == null) {
            frfRepositoryInstace = new FRFRepository();
        }
        return frfRepositoryInstace;
    }

//    TODO: add following methods to repository:
    public UFFDataSource saveUFFSource(String fileAddress) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            UFFDataSource resultUFF = new UFFDataSource(fileAddress);
            session.persist(resultUFF);
            ObjectMapper objectMapper = MainController.getObjectMapper();
            //Read data with Python
            byte[] pythonOutput = getPythonOutputByteArray(pythonizePathToFile(fileAddress, CommonMethods.PathFrom.SYSTEM));
            // Read the Python output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pythonOutput)))) {
                // Setting types of datasets
                String line = reader.readLine();
                List<Long> datasetTypes = Arrays.stream(line.trim().split(" "))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
                // Read and parse JSON data for each type
                for (int datasetTypeId = 0; datasetTypeId < datasetTypes.size(); datasetTypeId++) {
                    int timeout = 10;
                    while (!reader.ready()) {
                        timeout--;
                        Thread.sleep(1000);
                        if (timeout == 0) {
                            throw new RuntimeException("Timeout while waiting for BufferedReader output");
                        }
                    }
                    line = reader.readLine();
                    if (line == null || line.isEmpty()) {
                        datasetTypeId--;
                        continue;
                    }
                    Long datasetType = datasetTypes.get(datasetTypeId);
                    // Parse JSON data
                    try {
                        Class<?> uffClass = Class.forName(BASE_UFF_TYPES_CALSS_PATH + datasetType);
                        UFFDataset uffData = (UFFDataset) objectMapper.readValue(line, uffClass);
                        session.persist(uffData);
                        resultUFF.addUFFDataset(uffData);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Unsupported type of dataset: " + datasetType + "\n" + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
            }
            transaction.commit();
            return resultUFF;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving UNV file: " + e.getMessage(), e);
        }
    }

    public UFFDataSource getUFFSource(String fileAddress) {
        UFFDataSource uffSource = null;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM UFFDataSource WHERE sourceAddress = :fileAddress";
            Query<UFFDataSource> query = session.createQuery(hql, UFFDataSource.class);
            query.setParameter("fileAddress", fileAddress);
            uffSource = query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error getting UNV file: " + e.getMessage(), e);
        }
        return uffSource;
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

    protected byte[] getPythonOutputByteArray(String UFFPath) {
        Jep pythonInterpreter = PythonInterpreterService.getPythonInterpreter();
        ByteArrayOutputStream pythonOutput = PythonInterpreterService.getPythonOutputStream();
        String pythonScript = CommonMethods.getTextFileContent(PATH_OF_PYTHON_SCRIPT_FOR_UFF);
        pythonInterpreter.exec(pythonScript);
        pythonInterpreter.exec(String.format("parse_UFF('%s')", UFFPath));
        return pythonOutput.toByteArray();
    }
}