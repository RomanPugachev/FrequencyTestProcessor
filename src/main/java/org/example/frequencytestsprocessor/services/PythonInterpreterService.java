package org.example.frequencytestsprocessor.services;

import jep.Jep;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import lombok.Getter;

import java.io.ByteArrayOutputStream;

public class PythonInterpreterService {
    @Getter
    private static volatile Jep pythonInterpreter;  // Singleton instance of Jep interpreter
    @Getter
    private static volatile ByteArrayOutputStream pythonOutputStream; // Corresponding output stream

    static {
        System.out.println("Initializing Python interpreter...");
        try {
            // This might help add JEP dependency
            // -Djep.library.path=/home/roman/.pyenv/versions/3.12.5/lib/python3.12/site-packages/jep
            // -Dpython.home=/home/roman/.pyenv/versions/3.12.5
            // -Djava.library.path=/home/roman/.pyenv/versions/3.12.5/lib/python3.12/site-packages/jep
            System.out.println("Loading Python interpreter...\nPATH environment variable: " + System.getenv("PATH"));
            System.out.println("Working dir: " + System.getProperty("user.dir"));
            System.out.println("java.library.path = " + System.getProperty("java.library.path"));
            String JEP_HOME = System.getenv("JEP_HOME");
            JEP_HOME = "/home/roman/.pyenv/versions/3.12.5/lib/python3.12/site-packages/jep/libjep.so";
            MainInterpreter.setJepLibraryPath(JEP_HOME);
            pythonOutputStream = new ByteArrayOutputStream();
            JepConfig config = new JepConfig()
                    .setIncludePath("/home/roman/.pyenv/versions/3.12.5/lib/python3.12/site-packages")
                    .redirectStdout(pythonOutputStream);
            SharedInterpreter.setConfig(config);
            pythonInterpreter = new SharedInterpreter();
            healthCheck();
        } catch (Exception e) {
            try {
                if (pythonInterpreter != null) {
                    pythonInterpreter.close();
                }
                if (pythonOutputStream != null) {
                    pythonOutputStream.close();
                }
            } catch (Exception closeException) {
                e.addSuppressed(closeException);
            }
            throw new RuntimeException("Failed to initialize Python interpreter", e);
        }
    }

    public static void healthCheck() {
        System.out.println("Python interpreter health check...");
        pythonInterpreter.eval("import sys");
        pythonInterpreter.eval("import os");
        pythonInterpreter.eval("print('Python interpreter working directory' + os.getcwd())");
        pythonInterpreter.eval("print(sys.version)");
        pythonInterpreter.eval("print('Executable path: ' + sys.executable)");
        // List of available python packages installed by pip
        pythonInterpreter.eval("print('Python packages: ' + str(sys.modules.keys()))");
        // change working directory to the project root directory
        pythonInterpreter.eval("import os");
        pythonInterpreter.eval("import numpy");
        System.out.println("Python interpreter is healthy!");
    }
}
