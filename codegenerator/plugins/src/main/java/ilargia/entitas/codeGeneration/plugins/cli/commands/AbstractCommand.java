package ilargia.entitas.codeGeneration.plugins.cli.commands;

import ilargia.entitas.codeGeneration.interfaces.ICommand;

import java.util.Properties;

public abstract class AbstractCommand implements ICommand {


    protected Properties loadProperties() throws Exception {
        Properties prop = new Properties();
//        InputStream input = null;
//
//        try {
//            input = new FileInputStream(Preferences.PATH_PROPERTIES);
//            prop.load(input);
//
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return prop;
    }

    //
    protected boolean assertProperties() {
//        if (Path.fromPortableString(Preferences.PATH_PROPERTIES).toFile().exists()) {
//            return true;
//        }
//
//        System.out.printf("Warn: Couldn't find " + Preferences.PATH_PROPERTIES);
//        System.out.printf("Info: Run 'entitas new' to create Entitas.properties with default values");
//
        return false;
    }
}
