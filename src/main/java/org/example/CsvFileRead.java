package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvFileRead {
    final String PATH = "src/main/resources/country-list.csv";

    public List<Standort> csvRead(){

        List<Standort> records = new ArrayList<>();


        try {
            BufferedReader br = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8);
            String line = br.readLine();
            while (line != null) {
                String[] values = line.split(",");
                String stadt = values[1].replace("\"","");
                String land = values[0].replace("\"","");

                Standort standort = new Standort(land,stadt);
                records.add(standort);
                line=br.readLine();
            }
            System.out.println(records.toString());
            return records;
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
            return null;



    }




        }
    }

