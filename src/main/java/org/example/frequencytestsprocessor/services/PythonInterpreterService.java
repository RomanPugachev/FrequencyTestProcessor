package org.example.frequencytestsprocessor.services;

import jep.Jep;
import jep.JepConfig;
import lombok.Getter;
import org.example.frequencytestsprocessor.commons.StaticStrings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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
    public class JepLoader {
        private static final String LIB_NAME = "jep"; // 'jep.dll' or 'libjep.so' or 'libjep.dylib'

        public static void loadJep() {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                String libFileName;
                if (os.contains("win")) {
                    libFileName = "jep.dll";
                } else if (os.contains("mac")) {
                    libFileName = "libjep.dylib";
                } else {
                    libFileName = "libjep.so";
                }

                // Extract from JAR
                InputStream in = JepLoader.class.getResourceAsStream("/native/" + libFileName);
                if (in == null) {
                    throw new RuntimeException("Native library not found!");
                }

                Path tempFile = Files.createTempFile("jep_", libFileName);
                tempFile.toFile().deleteOnExit();
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

                // Load library
                System.load(tempFile.toAbsolutePath().toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to load Jep library", e);
            }
        }
    }
}
