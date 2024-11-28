package org.example.frequencytestsprocessor.commons;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        Long minId = 0L, maxId = 0L, numRows = (long) sensorsIDs.size();
        if (numRows.equals(0L)) return "F0";
        Set<Long> existingNums = new HashSet<>();
        Pattern regexPattern = Pattern.compile("^F\\d+$");
        sensorsIDs.forEach((s) -> {
            if (regexPattern.matcher(s).matches()) {
                existingNums.add(Long.parseLong(s.substring(1)));
            }
        }});
        return "";
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
