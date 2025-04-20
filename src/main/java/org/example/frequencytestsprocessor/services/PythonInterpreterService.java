package org.example.frequencytestsprocessor.services;

import jep.Jep;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import lombok.Getter;

import java.io.ByteArrayOutputStream;

/**
 * Service class for Python interpreter.
 * This class provides methods to initialize and interact with a Python interpreter.
* It also provides a method to execute Python code. Important note: for the interpreter to work, you need to set following environment variables:
  * 1. PYTHONPATH - path to Python packages: /home/user/.pyenv/versions/3.12.5/lib/python3.12/site-packages
  * 2. PYTHONHOME - path to Python installation: /home/user/.pyenv/versions/3.12.5
  * 3. JEP_HOME - path to JEP library: /home/user/.pyenv/versions/3.12.5/lib/python3.12/site-packages/jep/libjep.so */
public class PythonInterpreterService {
    @Getter
    private static volatile Jep pythonInterpreter;  // Singleton instance of Jep interpreter
    @Getter
    private static volatile ByteArrayOutputStream pythonOutputStream; // Corresponding output stream

    static {
        System.out.println("Initializing Python interpreter...");
        try {
            System.out.println("Loading Python interpreter...\nPATH environment variable: " + System.getenv("PATH"));
            System.out.println("Working dir: " + System.getProperty("user.dir"));
            System.out.println("java.library.path = " + System.getProperty("java.library.path"));

            pythonOutputStream = new ByteArrayOutputStream();
            JepConfig config = new JepConfig()
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
        pythonInterpreter.eval("print('Python packages: ' + str(sys.modules.keys()))");
        pythonInterpreter.eval("import numpy as np");
        System.out.println("Python interpreter output: " + pythonOutputStream.toString());
        pythonOutputStream.reset();
        System.out.println("Python interpreter is healthy!");
    }
}
