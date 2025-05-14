package org.example.frequencytestsprocessor.services.repositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Section;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.Sensor;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.SensorDataType;
import org.example.frequencytestsprocessor.datamodel.UFF58Repr.UFF58Representation;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.PipelineCalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.TimeSeriesBasedCalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.UFFBasedFRF;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents.AircraftModel;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
import org.example.frequencytestsprocessor.datamodel.formula.Formula;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import org.hibernate.Hibernate;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.CommonMethods.*;
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

    public UFFDataSource saveUFFSource(String fileAddress, AircraftModel parentAircraftModel) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Merge is unnecessary since we just loaded it
            UFFDataSource resultUFFsource = new UFFDataSource(fileAddress);
            session.persist(resultUFFsource);
            session.merge(parentAircraftModel);
            parentAircraftModel.addDataSource(resultUFFsource);

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
//                        TODO: implement saving FRF from UFF58
//                        if (uffData instanceof UFF58) {
//                            UFFBasedFRF uffBasedFRF = new UFFBasedFRF();
//                             prevent appearing error: jakarta.persistence.OptimisticLockException: org.hibernate.exception.LockAcquisitionException: error performing isolated work [[SQLITE_BUSY] The database file is locked (database is locked)] [n/a]
//                            session.persist(uffBasedFRF);
//                            uffBasedFRF.setParentUFF58Dataset((UFF58) uffData);
//                        }

                        resultUFFsource.addUFFDataset(uffData);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Unsupported type of dataset: " + datasetType + "\n" + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
            }
            transaction.commit();
            return resultUFFsource;
        } catch (Exception e) {
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

    public TimeSeriesDataSource saveTimeSeriesSourceFromCSV(String fileAddress, AircraftModel parentAircraftModel) {
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
            parentAircraftModel.addDataSource(resultTimeSeriesSource);

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
                }
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

    public TimeSeriesDataset getTimeSeriesDatasetById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(TimeSeriesDataset.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error getting TimeSeriesDataset by id: " + e.getMessage(), e);
        }
    }
    public Optional<TimeSeriesBasedCalculatedFrequencyDataRecord> saveTimeSeriesBasedCalculatedFrequencyDataRecord(TimeSeriesDataset parentTimeSeriesDataset, Double leftLimit, Double rightLimit, String name) {
        Transaction transaction = null;
        TimeSeriesBasedCalculatedFrequencyDataRecord incomingRecord = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.merge(parentTimeSeriesDataset);
            try {
                incomingRecord = new TimeSeriesBasedCalculatedFrequencyDataRecord(parentTimeSeriesDataset, leftLimit, rightLimit, name);
                parentTimeSeriesDataset.addChildFrequencyRecord(incomingRecord);
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error processing saving timeSeriesBasedCalculatedFrequencyDataRecord: " + e.getMessage(), e);
            }
            transaction.commit();
            return Optional.of(incomingRecord);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving timeSeriesBasedCalculatedFrequencyDataRecord: " + e.getMessage(), e);
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

    public synchronized Optional<PipelineCalculatedFrequencyDataRecord> savePipelineCalculatedFrequencyDataRecord(UFFDataSource uffDataSource, Formula formula, String sectionName, String typeName, Long currentRunId, ObservableList<Sensor> chosenSensors, FRF calculatedFRF) {
        Transaction transaction = null;
        PipelineCalculatedFrequencyDataRecord incomingRecord = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            incomingRecord = new PipelineCalculatedFrequencyDataRecord(formula, sectionName, typeName, currentRunId, uffDataSource, calculatedFRF);
            session.persist(incomingRecord);
            transaction.commit();
            session.detach(incomingRecord);
            return Optional.of(incomingRecord);
        } catch (Exception e) {
            throw new RuntimeException("Error saving pipelineCalculatedFrequencyDataRecord: " + e.getMessage(), e);
        }
    }

    /**
     * Method for getting AircraftModel by name. If AircraftModel with given name doesn't exist, you can specify createIfNotExists flag to create new AircraftModel.
     * @param aircraftModelName - name of AircraftModel
     * @return AircraftModel
     */
    public AircraftModel getAircraftModelByName(String aircraftModelName, boolean createIfNotExists) {
        Transaction transaction = null;
        AircraftModel aircraftModel = null;

        // First step: Check if model exists
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            try {
                aircraftModel = session.createQuery("select a from AircraftModel a where a.aircraftModelName = :name", AircraftModel.class)
                        .setParameter("name", aircraftModelName)
                        .uniqueResult();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error checking for existing AircraftModel: " + e.getMessage(), e);
            }
        }

        // Second step: Create new model if it doesn't exist and flag is set
        if (aircraftModel == null && createIfNotExists) {
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                try {
                    aircraftModel = new AircraftModel(aircraftModelName);
                    session.save(aircraftModel);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    throw new RuntimeException("Error creating new AircraftModel: " + e.getMessage(), e);
                }
            }
        } else if (aircraftModel == null) {
            throw new RuntimeException("AircraftModel with name " + aircraftModelName + " does not exist and createIfNotExists is false");
        }
        return aircraftModel;
    }

    public AircraftModel getAircraftModelById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(AircraftModel.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving AircraftModel by ID: " + e.getMessage(), e);
        }
    }


}