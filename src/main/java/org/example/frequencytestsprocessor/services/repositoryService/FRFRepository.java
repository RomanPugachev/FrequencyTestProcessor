package org.example.frequencytestsprocessor.services.repositoryService;

import org.example.frequencytestsprocessor.controllers.MainController;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFFDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FRFRepository {
    private MainController mainController;

    private SessionFactory sessionFactory;

    private static FRFRepository frfRepositoryInstace;

    public FRFRepository getRepository() {
        if (frfRepositoryInstace == null) {
            frfRepositoryInstace = new FRFRepository();
            frfRepositoryInstace.sessionFactory =
        }
        return frfRepositoryInstace;
    }

//    TODO: add following methods to repository:
    public UFFDataSource saveUFFSource(String fileAddress) {
        // Basic initialization
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        sessionFactory.
        UFFDataSource resultUFF = new UFF();
        ObjectMapper objectMapper = MainController.getObjectMapper();
        //Read data with Python
        byte[] pythonOutput = UFF.getPythonOutputByteArray(pythonizePathToFile(fileAddress, CommonMethods.PathFrom.SYSTEM));
        // Read the Python output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pythonOutput)))){
            // Setting types of datasets
            String line = reader.readLine();
            resultUFF.setTypesOfDatasets(
                    Arrays.stream(line.trim().split(" "))
                            .map(Long::valueOf)
                            .collect(Collectors.toList()));
            resultUFF.datasets = new ArrayList<>(resultUFF.typesOfDatasets.size()); resultUFF.toBeProcessedDatasetsIndices = new ArrayList<>(resultUFF.typesOfDatasets.size());
            // Read and parse JSON data for each type
            for (int datasetTypeId = 0; datasetTypeId < resultUFF.typesOfDatasets.size(); datasetTypeId++) {
                int timeout = 10;
                while (!reader.ready()){
                    timeout--;
                    Thread.sleep(1000);
                    if (timeout == 0) {
                        throw new RuntimeException("Timeout while waiting for BufferedReader output");
                    }
                }
                line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    datasetTypeId--;
                    continue; // Scip empty lines
                }
                Long datasetType = resultUFF.typesOfDatasets.get(datasetTypeId);
                // Parse JSON data
                try {
                    Class<?> uffClass = Class.forName(BASE_UFF_TYPES_CALSS_PATH + datasetType);
                    UFFDataset uffData = (UFFDataset) objectMapper.readValue(line, uffClass);
                    resultUFF.datasets.add(uffData);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unsupported type of dataset: " + datasetType + "\n" + e.getMessage(), e);
                }
                if (datasetType == 58) { resultUFF.toBeProcessedDatasetsIndices.add(Long.valueOf(datasetTypeId)); }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing UNV file: " + e.getMessage(), e);
        }
        return resultUFF;
    }

    protected static byte[] getPythonOutputByteArray(String UFFPath) {
        Jep pythonInterpreter = PythonInterpreterService.getPythonInterpreter();
        ByteArrayOutputStream pythonOutput = PythonInterpreterService.getPythonOutputStream();
        String pythonScript = CommonMethods.getTextFileContent(PATH_OF_PYTHON_SCRIPT_FOR_UFF);
        pythonInterpreter.exec(pythonScript);
        pythonInterpreter.exec(String.format("parse_UFF('%s')", UFFPath));
        return pythonOutput.toByteArray();
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