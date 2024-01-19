package org.example;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MySQLConnection {

    private  String host;
    private  String username;
    private  String dbName;
    private  String password;
    private  int port;

    private Connection connection;

   private String tableCity ="tbl_city", tableCountry = "tbl_country",
    columnCountry = "country", columnIdCountry = "idCountry", columnIdCity = "idCity", columnCity = "city";


      public MySQLConnection(Properties properties) {
        if (properties != null){
            host = properties.getProperty("host");
            username = properties.getProperty("username");
            dbName = properties.getProperty("dbName");
            password =properties.getProperty("password");
            port = Integer.valueOf(properties.getProperty("port"));

        }

    }
    public Connection getConnection() {
        if (this.connection == null) {
            String url = "jdbc:mysql://" + host + ":" + port + "/";
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error: Cannot connect to the database. " + ex.getMessage());
            }
        }
        return connection;
    }

    public Connection getConnectionIfDatabaseExists() {

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println("Error: Cannot connect to the database. 1" + ex.getMessage());
        }
        return connection;
    }
    public void createDB(){

            try {
                Statement stmt = getConnection().createStatement();

                String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbName; //creates database
                stmt.executeUpdate(createDatabase);

                stmt.close();
                connection.close();
                System.out.println("<< Database created successfully >>");
            }
            catch (SQLException exception){
                System.out.println("Konnte DB nicht anlegen " + exception.getMessage());
            }

        }
    public void createTables(){
        try {
            Statement stmt = getConnectionIfDatabaseExists().createStatement();


            String createTableCountries =  "CREATE TABLE " +
                    tableCountry +"( " +
                    columnIdCountry + " int NOT NULL AUTO_INCREMENT, " +
                    columnCountry + " varchar(255) NOT NULL, PRIMARY KEY (" + columnIdCountry + "))";


            String createTableCities = "CREATE TABLE " + tableCity + "( " +
                    columnIdCity + " int NOT NULL AUTO_INCREMENT, " +
                    columnCity + " varchar(255) NOT NULL," +
                    columnIdCountry + " int NOT NULL," +
                    "PRIMARY KEY (" +
                    columnIdCity + ")," +
                    "FOREIGN KEY (" +
                    columnIdCountry +
                    ") REFERENCES " + tableCountry +
                    "(" + columnIdCountry + "))";




            stmt.executeUpdate(createTableCountries);
            stmt.executeUpdate(createTableCities);

            stmt.close();
            connection.close();


            System.out.println("Tabellen angelegt");
        }
        catch (SQLException exception){
            System.out.println("Konnte Tabellen nicht anlegen " + exception.getMessage());
        }
    }
    public boolean addCountries(List<Standort> standorts){
        String country = null;
        try {
            Statement statement = getConnectionIfDatabaseExists().createStatement();

            for (int i = 0; i < standorts.size(); i++) {

                String querry = "SELECT " +
                        columnCountry + " from " +
                        tableCountry + " WHERE " +
                        columnCountry + " =" + "\"" +
                        standorts.get(i).getLand() + "\"";

                ResultSet resultSet = statement.executeQuery(querry);

                if (resultSet.next()) {
                    country = resultSet.getString(columnCountry);
                }

                if (!Objects.equals(country, standorts.get(i).getLand())) {
                    querry = "Insert into " + tableCountry +
                            "(" +
                            columnCountry + ") values (" + "\"" +
                            standorts.get(i).getLand() + "\");";

                    System.out.println(querry);
                    statement.executeUpdate(querry);
                }
            }
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Länder konnten nicht hinzugefügt werden " + e.getMessage());
            return false;
        }
    }

    public boolean addCities(List<Standort> locations) {

        ResultSet resultSet = null;
        int id = 99999999;
        String city = null;
        int countryForeignID = 888888;

        try {
            Statement statement = getConnectionIfDatabaseExists().createStatement();

            for (int i = 0; i <locations.size() ; i++) {

                //lese id des landes für betreffende Stadt aus

                String querry = "SELECT " + columnIdCountry + "," +
                        columnCountry + " from " +
                        tableCountry + " WHERE " +
                        columnCountry + " =" + "\"" +
                        locations.get(i).getLand() +"\"";

                resultSet = statement.executeQuery(querry);
                if (resultSet.next()){
                    id = resultSet.getInt(columnIdCountry);
                }

                // suche ob stadt bereits existiert
                querry = "SELECT " + columnCity + "," +
                        columnIdCountry + " from " +
                        tableCity + " WHERE " +
                        columnCity + "= \"" +
                        locations.get(i).getStadt() +"\"";


                resultSet = statement.executeQuery(querry);

                if (resultSet.next()){
                    city = resultSet.getString(columnCity);
                    countryForeignID = resultSet.getInt((columnIdCountry));
                }


                // speichere stadt ein wenn nicht vorhanden, oder bei gleichem namen aber in einem anderen land
                // @todo im moment gibt es keine begrenzung für städte mit gleichen namen aber unterschiedlichen Land
                if (!Objects.equals(city, locations.get(i).getStadt()) || (Objects.equals(city,locations.get(i).getStadt())
                        && countryForeignID != id))
                {
                    querry = "Insert into " + tableCity +
                            "(" +
                            columnCity +"," + columnIdCountry + ") values (" + "\"" +
                            locations.get(i).getStadt() + "\", " +
                            id +
                            ");";

                    statement.executeUpdate(querry);
                }
            }
            statement.close();
            System.out.println("Städte wurden gespeichert.");
            return true;
        }
        catch (SQLException e){
            System.out.println("Städte konnten nicht hinzugefügt werden. " + e.getMessage());
            return false;
        }
    }
    }

