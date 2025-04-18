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
If you want to build project from sources or you intend to develop this project, you need to install following tools:
- JDK 21
- Maven 3.8.7
- Python 3.12.5
    - pyuff 2.4.6
    - numpy 2.2.4
    - jep 4.2.2

### Important issure: installation of JEP
This project uses JEP library for parsing expressions in uff files. If you want to build project from sources or you intend to develop this project, you need to install JEP library step-by-step. Suppose, you already have JDK, Maven and Python installed.