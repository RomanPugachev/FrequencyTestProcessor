package org.example.frequencytestsprocessor.commons;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonMethods {
    public static void print(Object ... objects) {
        for (Object object : objects) {
            System.out.println(object);
        }
    }
    public static File getFileFromDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с данными");
        return fileChooser.showOpenDialog(new Stage());
    }
    public static void showAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void showAlertUnimplemented(){
        showAlert("Ошибка", "Ошибка произведения рассчета", "Пока что функция произведения рассчёта не реализована");
    }
    public static void printByteArrayOutputStram(ByteArrayOutputStream outputStream) throws IOException {
        var resultByteArray = outputStream.toByteArray();
        try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resultByteArray)))){
            while (inputStreamReader.ready()) {
                System.out.println(inputStreamReader.readLine());
            }
        }
    }
    public static String getTextFileContent(String pathToFile) {
        try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile)))){
            StringBuilder stringBuilder = new StringBuilder();
            while (inputStreamReader.ready()) {
                stringBuilder.append(inputStreamReader.readLine() + "\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String pythonizePathToFile(String pathToFile, CommonMethods.PathFrom pathFrom){
        /* "C:\\Temp\\test_uff.uff" -> "C:\\\\Temp\\\\test_uff.uff" */
        if (pathFrom.equals(PathFrom.PYTHON)) {
            return pathToFile;
        } else if (pathFrom == PathFrom.JAVA || pathFrom ==PathFrom.SYSTEM){
            return pathToFile.replace("\\", "\\\\");
        } else throw new RuntimeException("Unsupported pathFrom value");
    }

    public static enum PathFrom {
        JAVA,
        PYTHON,
        SYSTEM
    }

    public static String generateId (List<String> sensorsIDs) {
        long minId = Long.MAX_VALUE;
        long maxId = Long.MIN_VALUE;
        Long numRows = (long) sensorsIDs.size();
        if (numRows.equals(0L)) return "F0";
        Set<Long> existingNums = new HashSet<>();
        Pattern regexPattern = Pattern.compile("^F\\d+$");
        // Searching for existing indexes
        for (String s : sensorsIDs) {
            if (regexPattern.matcher(s).matches()) {
                Long curNum = Long.parseLong(s.substring(1));
                existingNums.add(curNum);
                minId = Math.min(minId, curNum);
                maxId = Math.max(maxId, curNum);
            }
        }
        // Generating new index
        if (minId > 0L) {
            return "F0";
        } else {
            Long newId = minId + 1L;
            while (existingNums.contains(newId)) {
                newId++;
            }
            return "F" + newId;
        }
    }
//    public static void main(String[] args) {
//        String pathPython = "C:\\\\Temp\\\\test_uff.uff";
//        String pathJava = "C:\\Temp\\test_uff.uff";
//        String pathSystem = new File("C:\\Temp\\test_uff.uff").getAbsolutePath();
//        String transtaltedPathFromPython = pythonizePathToFile(pathPython, PathFrom.PYTHON);
//        String transtaltedPathFromJava = pythonizePathToFile(pathJava, PathFrom.JAVA);
//        String transtaltedPathFromSystem = pythonizePathToFile(pathSystem, PathFrom.SYSTEM);
//    }
}
