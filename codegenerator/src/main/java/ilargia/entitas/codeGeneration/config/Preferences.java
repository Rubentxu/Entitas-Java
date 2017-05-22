package ilargia.entitas.codeGeneration.config;

import java.io.*;
import java.util.Properties;

public class Preferences {

    public static String PROJECT_PATH = "./";
    public static String SOURCE_PATH = "src/main/java/";
    public static String PATH_PROPERTIES = "Entitas.properties";
    private static Properties prop = new Properties();
    private static InputStream input = null;
    private static OutputStream output = null;


    public static boolean hasProperties() {
        return new File(PROJECT_PATH + "/" + PATH_PROPERTIES).exists();
    }

    public static Properties loadProperties() {
        try {
            prop.load(new FileInputStream(PROJECT_PATH + "/" + PATH_PROPERTIES));
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
            properties.store(new FileOutputStream(PROJECT_PATH + "/" + PATH_PROPERTIES), null);
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
