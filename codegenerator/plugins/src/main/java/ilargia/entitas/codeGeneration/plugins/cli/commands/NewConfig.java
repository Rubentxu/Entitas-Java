package ilargia.entitas.codeGeneration.plugins.cli.commands;


import ilargia.entitas.codeGeneration.cli.ArgsExtension;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public class NewConfig extends AbstractCommand {
    @Override
    public String trigger() {
        return "new";
    }

    @Override
    public String description() {
        return "Creates new Entitas.properties config with default values";
    }

    @Override
    public String example() {
        return "entitas new [-f]";
    }

    @Override
    public void run(String[] args) {

        Path currentRelativePath = Paths.get("");
        String currentDir = currentRelativePath.toAbsolutePath().toString();
        File path = new File(currentDir + "/"/* + Preferences.PATH_PROPERTIES*/);

        if (ArgsExtension.isForce(Arrays.asList(args)) || !path.exists()) {
            CodeGeneratorConfig defaultConfig = new CodeGeneratorConfig();
            Properties properties = new Properties(defaultConfig.defaultProperties());
            defaultConfig.setProperties(properties);

            saveProperties(properties, path.getAbsolutePath());


            new EditConfig().run(args);
        } else {
            System.out.println(path + " already exists!");
            System.out.println("Use entitas new -f to overwrite the exiting file.");
            System.out.println("Use entitas edit to open the exiting file.");
        }
    }


    private void saveProperties(Properties props, String path) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(path);
            props.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            System.out.println("Created " + path);
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
