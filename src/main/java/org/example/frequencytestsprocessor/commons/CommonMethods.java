package org.example.frequencytestsprocessor.commons;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

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
    public static void main(String[] args) {
        String pathPython = "C:\\\\Temp\\\\test_uff.uff";
        String pathJava = "C:\\Temp\\test_uff.uff";
        String pathSystem = new File("C:\\Temp\\test_uff.uff").getAbsolutePath();
        String transtaltedPathFromPython = pythonizePathToFile(pathPython, PathFrom.PYTHON);
        String transtaltedPathFromJava = pythonizePathToFile(pathJava, PathFrom.JAVA);
        String transtaltedPathFromSystem = pythonizePathToFile(pathSystem, PathFrom.SYSTEM);
    }
}
