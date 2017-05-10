package com.ilargia.games.entitas.codeGenerator.configuration;

import java.io.*;
import java.util.Properties;

public class Preferences {

    public static String PATH = "Entitas.properties";
    private static Properties prop = new Properties();
    private static InputStream input = null;
    private static OutputStream output = null;


    public static boolean hasProperties() {
        return new File(PATH).exists();
    }

    public static Properties loadProperties() {
        try {
            prop.load(new FileInputStream(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    public static void saveProperties(Properties properties) {
        try {
            properties.store(new FileOutputStream(PATH), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
