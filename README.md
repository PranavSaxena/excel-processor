# Excel Processor

A simple Spring Boot application to process Excel files.

## Features
- Upload an Excel (.xlsx) file.
- Converts "Required Tasks" column into JSON formatted "Rules".
- Generates a new Excel file with the "Rules" column.
- Includes unit tests for rule conversion logic.

## How to Run
1. Build the project:
mvn clean install

2. Run the application:
mvn spring-boot:run

3. Use POST `/api/excel/upload` to upload an Excel file.

## Technologies Used
- Java
- Spring Boot
- Apache POI
- Jackson
- JUnit 5
