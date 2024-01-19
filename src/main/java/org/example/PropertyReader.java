package org.example;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final String PATH = "src/main/resources/config.properties";
    public static Properties readProperties(){
        try {
            InputStream input = new FileInputStream(PATH);

            Properties properties = new Properties();
            properties.load(input);
            System.out.println(properties.get("host"));
            return  properties;

        }
        catch (Exception exception){
            System.out.println("Error: Cannot read properties!\n" + exception.getMessage());
            System.exit(-1);
            return null;

        }
    }
}
