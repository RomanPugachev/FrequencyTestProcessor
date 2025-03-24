package org.example.frequencytestsprocessor.commons;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import org.example.frequencytestsprocessor.services.languageService.LanguageNotifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DOT;
import static org.example.frequencytestsprocessor.commons.StaticStrings.OTHER;

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
    public static void showSuccess(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showAlertIdAlreadyExists(String detailedMessage){
       showAlert("Ошибка", "Ошибка повторения существующего элемента", detailedMessage);
    }
    public static void showAlertUnimplemented(){
        showAlert("Ошибка", "Ошибка выполнения функции", "Пока что данная функция не реализована.");
    }
    public static void printByteArrayOutputStram(ByteArrayOutputStream outputStream) throws IOException {
        var resultByteArray = outputStream.toByteArray();
        try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resultByteArray)))){
            while (inputStreamReader.ready()) {
                System.out.println(inputStreamReader.readLine());
            }
        }
    }

    public static String getDecodedProperty(Properties lp, String FULL_PROPERTY_ID) {
        String text = lp.getProperty(FULL_PROPERTY_ID);
        if (text != null) {
            byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
            return new String(bytes, StandardCharsets.UTF_8);
        } else {
            throw new RuntimeException("Ошибка при чтении файла с языком");
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

    public static String convertComplexListToString(List<Complex> complexList) {
        if (complexList == null || complexList.isEmpty()) return "";
        return complexList.stream()
                .map(Complex::toString) // Convert each Complex to string
                .collect(Collectors.joining(";")); // Join using semicolon
    }

    public static List<Complex> convertStringToComplexList(String complexesString) {
        if (complexesString == null || complexesString.isEmpty()) return List.of();
        return Arrays.stream(complexesString.split(";"))
                .map(Complex::fromString) // Convert each string to Complex
                .collect(Collectors.toList());
    }

    public static String convertListOfDoubleToString(List<Double> doubles) {
        return doubles.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    public static List<Double> convertStringToListOfDouble(String doublesString) {
        return Arrays.stream(doublesString.split(";"))
                .map(Double::valueOf)
                .toList();
    }

    public static enum PathFrom {
        JAVA,
        PYTHON,
        SYSTEM
    }

    public


//    public static void main(String[] args) {
//        String pathPython = "C:\\\\Temp\\\\test_uff.uff";
//        String pathJava = "C:\\Temp\\test_uff.uff";
//        String pathSystem = new File("C:\\Temp\\test_uff.uff").getAbsolutePath();
//        String transtaltedPathFromPython = pythonizePathToFile(pathPython, PathFrom.PYTHON);
//        String transtaltedPathFromJava = pythonizePathToFile(pathJava, PathFrom.JAVA);
//        String transtaltedPathFromSystem = pythonizePathToFile(pathSystem, PathFrom.SYSTEM);
//    }
}
