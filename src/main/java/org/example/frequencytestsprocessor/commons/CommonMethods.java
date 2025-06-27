package org.example.frequencytestsprocessor.commons;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import org.example.frequencytestsprocessor.helpers.languageHelper.LanguageObserver;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

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
        if (doubles == null || doubles.isEmpty()) return "";
        return doubles.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    public static List<Double> convertStringToListOfDouble(String doublesString) {
        if (doublesString == null || doublesString.isEmpty()) return List.of();
        return Arrays.stream(doublesString.split(";"))
                .map(Double::valueOf)
                .toList();
    }

    public static String convertListOfLongToString(List<Long> longs) {
        if (longs == null || longs.isEmpty()) return "";
        return longs.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    public static List<Long> convertStringToListOfLong(String longsString) {
        if (longsString == null || longsString.isEmpty()) return List.of();
        return Arrays.stream(longsString.split(";"))
                .map(Long::valueOf)
                .toList();
    }

    public static enum PathFrom {
        JAVA,
        PYTHON,
        SYSTEM
    }


    public static Charset defineCSVHeadersCharset(Path filePath, String valueDelimiter) {
        if (Files.notExists(filePath)) {
            throw new RuntimeException("File not found: " + filePath);
        }
        if (!Files.isReadable(filePath)) {
            throw new RuntimeException("File is not readable: " + filePath);
        }
        if (!filePath.getFileName().toString().endsWith(".csv")) {
            throw new RuntimeException("File is not CSV: " + filePath);
        }

        Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("windows-1251")};

        for (Charset charset : charsets) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), charset))) {
                String firstLine = reader.readLine();
                if (firstLine != null) {
                    String[] headers = firstLine.split(valueDelimiter);
                    boolean validEncoding = true;
                    for (String header : headers) {
                        if (!isValidHeader(header)) {
                            validEncoding = false;
                            break;
                        }
                    }
                    if (validEncoding && containsCyrillicCharacters(firstLine)) {
                        return charset;
                    }
                }
            } catch (IOException e) {
                // Continue to the next charset if there's an error
            }
        }

        // If no valid charset is found, return UTF-8 as a default
        return StandardCharsets.UTF_8;
    }

    private static boolean isValidHeader(String header) {
        // Check if the header contains only valid characters (Latin, Cyrillic, digits, and common symbols)
        return header.matches("^[\\p{L}\\p{N}\\s_№.]+$") && !header.contains("�");
    }

    private static boolean containsCyrillicCharacters(String text) {
        return text.matches(".*[А-Яа-я].*");
    }


    public static List<String[]> readAllLinesFromCSV(Path filePath, String valueDelimiter) {
        if (Files.notExists(filePath)) {
            throw new RuntimeException("File not found: " + filePath);
        }
        if (!Files.isReadable(filePath)) {
            throw new RuntimeException("File is not readable: " + filePath);
        }
        if (!filePath.getFileName().toString().endsWith(".csv")) {
            throw new RuntimeException("File is not CSV: " + filePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), defineCSVHeadersCharset(filePath, valueDelimiter)))) {
            List<String[]> lines = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                String[] values = line.split(valueDelimiter);
                // Process the values as needed
                lines.add(values);
            }

            return lines;
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
    }

    public static double getTimeRangeOfBorderedSeries(List<Double> timeStamps, Double leftBorder, Double rightBorder) {
        Iterator<Double> sourceTimeStampsIterator = timeStamps.iterator();
        Double firstTimeStamp = null;
        Double lastTimeStamp = null;
        int numberOfTimeStamps = 0;

        while (sourceTimeStampsIterator.hasNext()) {
            double timeStamp = sourceTimeStampsIterator.next();
            if (timeStamp >= leftBorder && timeStamp <= rightBorder) {
                if (firstTimeStamp == null) {
                    firstTimeStamp = timeStamp;
                }
                numberOfTimeStamps++;
                lastTimeStamp = timeStamp;
            } else if (timeStamp > rightBorder) {
                break;
            }
        }
        return lastTimeStamp - firstTimeStamp;
    }

    public static List<Double> getFrequenciesOfBorderedSeries(List<Double> timeStamps, Double leftBorder, Double rightBorder) {
        double timeRange = getTimeRangeOfBorderedSeries(timeStamps, leftBorder, rightBorder);
        List<Double> frequencies = new LinkedList<>();
        Iterator<Double> sourceTimeStampsIterator = timeStamps.iterator();
        long acceptedTimeStamps = 0;
        while (sourceTimeStampsIterator.hasNext()) {
            double timeStamp = sourceTimeStampsIterator.next();
            if (timeStamp >= leftBorder && timeStamp <= rightBorder) {
                frequencies.add(acceptedTimeStamps / timeRange);
                acceptedTimeStamps++;
            } else if (timeStamp > rightBorder) {
                break;
            }
        }
        return frequencies;
    }

    public static List<Double> getDataForFourierTransforms(List<Double> timeData, List<Double> timeStamps, Double leftBorder, Double rightBorder) {
        List<Double> dataForTransformation =new LinkedList<>();

        Iterator<Double> timeDataIterator = timeData.iterator();
        Iterator<Double> sourceTimeStampsIterator = timeStamps.iterator();

        while (timeDataIterator.hasNext() && sourceTimeStampsIterator.hasNext()) {
            double timeStamp = sourceTimeStampsIterator.next();
            double datasetValue = timeDataIterator.next();
            if (timeStamp >= leftBorder && timeStamp <= rightBorder) {
                dataForTransformation.add(datasetValue);
            }
        }
        return dataForTransformation;
    }


    // Begin to write here javadoc string
    /**
     *  These functions must support LineChart features
     */
    public static void zoomToArea(LineChart<Number, Number> chart, double xMin, double xMax, double yMin, double yMax) {
        ((NumberAxis) chart.getXAxis()).setLowerBound(xMin);
        ((NumberAxis) chart.getXAxis()).setUpperBound(xMax);
        ((NumberAxis) chart.getYAxis()).setLowerBound(yMin);
        ((NumberAxis) chart.getYAxis()).setUpperBound(yMax);
    }

    public static LanguageObserver observeAxis(NumberAxis axis) {
        return (languageProperties, languageToSet, previousLanguage) -> {
            String key = axis.getId() + DOT;
            String text = languageProperties.getProperty(key + languageToSet);
            if (text != null) {
                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                axis.setLabel(decodedText);
            } else {
                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
            }
        };
    }

    public static LanguageObserver observeChartTitle(LineChart<?, ?> chart) {
        return (languageProperties, languageToSet, previousLanguage) -> {
            String key = chart.getId() + DOT + TITLE + DOT;
            String text = languageProperties.getProperty(key + languageToSet);
            if (text != null) {
                byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
                String decodedText = new String(bytes, StandardCharsets.UTF_8);
                chart.setTitle(decodedText);
            } else {
                throw new RuntimeException(String.format("It seems, renaming impossible for object with id %s", key));
            }
        };
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
