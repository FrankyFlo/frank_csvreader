package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvFileRead fileRead = new CsvFileRead();
        List<Standort> standorts = fileRead.csvRead();
        MySQLConnection sqlConnection = new MySQLConnection(PropertyReader.readProperties());
        sqlConnection.createDB();
        sqlConnection.createTables();
        sqlConnection.addCountries(standorts);
        sqlConnection.addCities(standorts);
    }



}