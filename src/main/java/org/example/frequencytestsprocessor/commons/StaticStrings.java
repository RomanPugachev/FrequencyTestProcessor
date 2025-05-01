package org.example.frequencytestsprocessor.commons;

public class StaticStrings {
    public static final String APPLICATION_NAME = "applicationName";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String UNDERLINE = "_";
    public static final String RU = "ru";
    public static final String EN = "en";
    public static final String LANGUAGE = "language";
    public static final String LANGUAGES = "languages";
    public static final String PROPERTIES = "properties";
    public static final String MAIN_APPLICATION_NAME = "mainApplicationName";
    public static final String UFF_STATIC_STRING = "UFF";
    public static final String PATH_TO_LANGUAGES = "/org/example/frequencytestsprocessor/language";
    public static final String PATH_OF_PYTHON_SCRIPT_FOR_UFF = "src/main/resources/org/example/frequencytestsprocessor/pythonScripts/UFFReadingUtils.py";
    public static final String BASE_UFF_TYPES_CALSS_PATH = "org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFF";
    public static final String CALCULATIONS_DIALOG_TITLE = "tempStage.calculationDialog";
    public static final String TIMESERIES_FRF_EXTRACTION_DIALOG_TITLE = "tempStage.timeSeriesFRFExtractionDialog";
    public static final String TYPES_OF_DATASETS = "typesOfDatasets";
    public static final String TO_BE_PROCESSED_DATASETS_INDICES = "toBeProcessedDatasetsIndices";
    public static final String DATASETS = "datasets";
    public static final String DEFAULT = "default";
    public static final String DEFAULT_SECTION_ID = "section.default";
    public static final String DEFAULT_TYPE_ID = "type.default";
    public static final String OTHER = "other";
    public static final String DEFAULT_GRAPHS_SENSOR_CHOICE = "defaultGraphsSensorChoice";
    public static final String DEFAULT_GRAPHS_RUN_CHOICE = "defaultGraphsRunChoice";
    public static final String DEFAULT_GRAPHS_TYPE_CHOICE = "defaultGraphsTypeChoice";
    public static final String BODE = "bode";
    public static final String NYQUIST = "nyquist";
    public static final String DEFAULT_CALCULATED_DATA_SOURCE = "defaultCalculatedDataSource";
    public static final String TITLE = "title";
    public static final String DEFAULT_CALCULATED_DATA_SOURCE_VALUE = "DEFAULT CALCULATED VALUE";



    public static final String VALID_PYTHON_SCRIPT = "import sys\n" +
            "import json\n" +
            "import pyuff\n" +
            "import numpy as np\n" +
            "import os\n" +
            "\n" +
            "def jsonalzie_list_with_complex(complex_list):\n" +
            "    return [{\"real\": possible_complex.real, \"imag\": possible_complex.imag} if isinstance(possible_complex, complex) else possible_complex\n" +
            "            for possible_complex in complex_list]\n" +
            "\n" +
            "def jsonalize_set(incoming_set):\n" +
            "    return {k: jsonalzie_list_with_complex(v.tolist()) if isinstance(v, np.ndarray) else v\n" +
            "            for k, v in incoming_set.items()}\n" +
            "\n" +
            "\n" +
            "def parse_UFF(file_path):\n" +
            "    unv_file = pyuff.UFF(file_path)\n" +
            "    print(str(unv_file.get_set_types())[1:-1])\n" +
            "    for i in range(unv_file.get_n_sets()):\n" +
            "        current_set = unv_file.read_sets(i)\n" +
            "        jsonalizable_dict = jsonalize_set(current_set)\n" +
            "        print(json.dumps(jsonalizable_dict))\n" +
            "        print(\"END_OF_JSON\")\n" +
            "parse_UFF('C:\\\\Temp\\\\test_uff.uff');";
}
