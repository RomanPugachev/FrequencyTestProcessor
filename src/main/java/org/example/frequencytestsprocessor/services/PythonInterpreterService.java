package org.example.frequencytestsprocessor.services;

import jep.Jep;
import jep.JepConfig;
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
            pythonOutputStream = new ByteArrayOutputStream();
            pythonInterpreter = new JepConfig().redirectStdout(pythonOutputStream).createSubInterpreter();
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
}
