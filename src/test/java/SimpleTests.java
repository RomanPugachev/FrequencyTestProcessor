import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jep.Jep;
import jep.JepConfig;
import jep.MainInterpreter;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFFDataset;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.example.frequencytestsprocessor.commons.CommonMethods.print;
import static org.example.frequencytestsprocessor.commons.CommonMethods.readAllLinesFromCSV;
import static org.example.frequencytestsprocessor.commons.StaticStrings.BASE_UFF_TYPES_CALSS_PATH;

@Getter
@Setter
public class SimpleTests {
    public static void main(String[] args) {
        // Check PYTHONHOME environment variable
//        String jepHome = System.getenv("JEPHOME");
        // Create Jep interpreter
        ByteArrayOutputStream pythonOutputStream = new ByteArrayOutputStream();
        Jep pythonInterpreter = new JepConfig().redirectStdout(pythonOutputStream).createSubInterpreter();
    }
//        ObjectMapper objectMapper = new ObjectMapper();
//        String line = "{\"type\": 151, \"model_name\": \"ssj-new_2023\", \"description\": \"NONE\", \"db_app\": \"Simcenter Testlab Rev work-21B\", \"date_db_created\": \"28-Apr-23\", \"time_db_created\": \"13:21:26\", \"version_db1\": 0, \"version_db2\": 0, \"file_type\": 0, \"date_db_saved\": \"28-Apr-23\", \"time_db_saved\": \"13:21:26\", \"program\": \"Simcenter Testlab Rev work-21B\", \"date_file_written\": \"29-Jun-23\", \"time_file_written\": \"11:56:03\"}";
//
//        try {
//            Class<?> uffClass = Class.forName(BASE_UFF_TYPES_CALSS_PATH + 151);
//            UFFDataset uffData = (UFFDataset) objectMapper.readValue(line, uffClass);
//            print(uffData.toString());
//        }
//        catch (ClassNotFoundException e) {
//            print(e.getMessage());
//        } catch (JsonProcessingException e) {
//            print(e.getMessage());
//        }
}