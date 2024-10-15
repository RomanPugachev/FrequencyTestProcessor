import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleTests {
    private int intVal = 8;
    private String stringVal = 10 + "";

    public static void main(String[] args) {
        SimpleTests tests = new SimpleTests();
        System.out.println(tests.getIntVal());
        System.out.println("Hello, world!");
    }
    public static void printCurrentWorkingDirectory() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentWorkingDirectory);
    }
}

