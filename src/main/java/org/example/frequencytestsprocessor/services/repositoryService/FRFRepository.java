package org.example.frequencytestsprocessor.services.repositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.TimeSeriesBasedCalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.pythonizePathToFile;
import static org.example.frequencytestsprocessor.commons.CommonMethods.readAllLinesFromCSV;
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

    public TimeSeriesDataSource saveTimeSeriesSourceFromCSV(String fileAddress) {
        // TODO: debug saving time series source from CSV
        if (fileAddress == null || fileAddress.isEmpty()) {
            throw new IllegalArgumentException("File address cannot be null or empty");
        }
        if (!fileAddress.endsWith(".csv")) {
            throw new IllegalArgumentException("File must be a CSV file");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            TimeSeriesDataSource resultTimeSeriesSource = new TimeSeriesDataSource(fileAddress);
            session.persist(resultTimeSeriesSource);

            List<String[]> allLinesInFile = readAllLinesFromCSV(Path.of(resultTimeSeriesSource.getSourceAddress()), ";");

            String[] headersLine = allLinesInFile.get(0);
            for(int i = 2; i < headersLine.length; i++) {
                TimeSeriesDataset timeSeriesDataset = new TimeSeriesDataset(headersLine[i]);
                resultTimeSeriesSource.addTimeSeriesDataset(timeSeriesDataset);
                session.persist(resultTimeSeriesSource.getTimeSeriesDatasets().get(i - 2));
            }
            List<String[]> dataLinesInFile = allLinesInFile.subList(1, allLinesInFile.size());
            for (String[] line : dataLinesInFile) {
                List<Double> lineDoubles = Arrays.stream(line)
                        .map(s -> Double.valueOf(s.replace(",", ".")))
                        .toList();
                resultTimeSeriesSource.addTimeStamps1(lineDoubles.get(0));
                resultTimeSeriesSource.addTimeStamps2(lineDoubles.get(1));
                for(int currentDatasetId = 0; currentDatasetId < resultTimeSeriesSource.getTimeSeriesDatasets().size(); currentDatasetId++) {
                    TimeSeriesDataset datasetForInserting = resultTimeSeriesSource.getTimeSeriesDatasets().get(currentDatasetId);
                    datasetForInserting.addTimeData(lineDoubles.get(currentDatasetId + 2));
//                    session.persist(datasetForInserting);
                }
                // Debugging
        //        if (line == dataLinesInFile.get(dataLinesInFile.size() - 1)) {
        //            break;
        //        }
            }
            // Check of existing datasets
            resultTimeSeriesSource.getTimeSeriesDatasets().stream()
                            .forEach(timeSeriesDataset -> {
                                if (timeSeriesDataset.getTimeData() == null || timeSeriesDataset.getTimeData().size() == 0) {
                                    throw new RuntimeException("Time data is null or empty");
                                }
                            });
            transaction.commit();
            return resultTimeSeriesSource;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving Time series datasource: " + e.getMessage(), e);
        }
    }

    public void saveFRF(String frf) {
        // Implement the logic to save the FRF to the database
    }

    public void saveTimeSeriesBasedCalculatedFrequencyDataRecord(TimeSeriesDataset parentTimeSeriesDataset, Double leftLimit, Double rightLimit, String name) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(parentTimeSeriesDataset);
            try {
                TimeSeriesBasedCalculatedFrequencyDataRecord incomingRecord = new TimeSeriesBasedCalculatedFrequencyDataRecord(parentTimeSeriesDataset, leftLimit, rightLimit, name);
                parentTimeSeriesDataset.addChildFrequencyRecord(incomingRecord);
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
            }
            transaction.commit();
            return;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving UNV file: " + e.getMessage(), e);
        }
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