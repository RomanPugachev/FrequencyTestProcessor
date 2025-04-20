# FrequencyTestsProcessor. What is it?
This project implements a user interface for convenient processing of frequency and timeseries data in different formats(**.css**, **.uff** and more).

Processing includes:
- Building graphs according to the data
- Saving data to database
- Calculation of expressions with frequency data  treated as complex functions
- Performing of fourier transforms with timeseries data
- Wide support of .uff files with setting up calculation by runs according to user-defined expressions and parameters


More features will be added in the future.

# Building and running
If you want to build project from sources or you want to participate in development, you need to install following tools:
- JDK 21
- Maven 3.8.7
- Python 3.12.5
    - pyuff 2.4.6
    - numpy 2.2.4
    - jep 4.2.0
- Any IDE with Maven support

### Important issure: installation of JEP
This project uses JEP library for parsing expressions in uff files. It might be difficult for the frst time to link JEP to application, so here is step-by-step guide, which will allow you setup work environment.

#### 0. Install any IDE with Maven support and get project sources with ```git clone https://github.com/RomanPugachev/FrequencyTestProcessor.git```

#### 1. Install JDK 21, python 3.12.5 and maven 3.8.7
Depending on your OS, you can download them from the links below:
- Java can be downloaded from [here](https://www.oracle.com/cis/java/technologies/downloads/#java21).
- Python can be downloaded from [here](https://www.python.org/downloads/).
- Maven can be downloaded from [here](https://maven.apache.org/download.cgi).
#### 2. Install python packages by run
```
pip install numpy==2.2.4
```
```
pip install pyuff==2.4.6
```
```
pip install jep==4.2.0
```

#### 3. Setup following environment variables for JEP:
- JAVA_HOME="/path/to/used/jdk" (e.g. `/home/user/.jdks/openjdk-21.0.1`)
- JEP_HOME="/path/to_python_site_packages/site-packages/jep/{libjep.so,  libjep.jnilib,  jep.dll depending on OS}" (e.g. `/home/user/.pyenv/versions/3.12.5/lib/python3.12/site-packages/jep/libjep.so`)
- PYTHONHOME=/path/to_python_directory (e.g. `/home/user/.pyenv/versions/3.12.5`)
- PYTHONPATH=/path/to/python_site-packages (e.g. `/home/user/.pyenv/versions/3.12.5/lib/python3.12/site-packages`)
