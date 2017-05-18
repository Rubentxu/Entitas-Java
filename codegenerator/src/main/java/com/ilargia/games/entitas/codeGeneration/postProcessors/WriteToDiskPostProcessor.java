package com.ilargia.games.entitas.codeGeneration.postProcessors;


import com.ilargia.games.entitas.codeGeneration.data.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import com.ilargia.games.entitas.codeGeneration.config.TargetDirectoryConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class WriteToDiskPostProcessor implements ICodeGenFilePostProcessor, IConfigurable {

    TargetDirectoryConfig _targetDirectoryConfig = new TargetDirectoryConfig();

    @Override
    public Properties getDefaultProperties() {
        return _targetDirectoryConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        _targetDirectoryConfig.configure(properties);
    }

    @Override
    public String getName() {
        return "Write to disk";
    }

    @Override
    public Integer gePriority() {
        return 100;
    }

    @Override
    public boolean isEnableByDefault() {
        return true;
    }

    @Override
    public boolean runInDryMode() {
        return false;
    }

    @Override
    public List<CodeGenFile> postProcess(List<CodeGenFile> files) {
        for (CodeGenFile file : files) {
//            IPath targetDir = Path.fromPortableString(_targetDirectoryConfig.targetDirectory());
//            if (!targetDir.toFile().exists()) {
//                targetDir.toFile().mkdir();
//            }
            createFile(file.fileName, _targetDirectoryConfig.targetDirectory(), file.fileContent.toString());
        }
        return files;
    }

    public static void toFile(JavaClassSource javaClass, File targetDir) {
        File f = targetDir;
        String[] parts = javaClass.getPackage().split("\\.");

        try {
            if (!targetDir.getAbsolutePath().endsWith(parts[parts.length - 1])) {
                f = new File(f, parts[parts.length - 1]);
                createParentDirs(f);
            }
            f = new File(f, javaClass.getName() + ".java");
            write(f, javaClass.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createParentDirs(File file) throws IOException {
        if (file != null) {
            File parent = file.getCanonicalFile();
            if (parent == null) {
                return;
            }
            parent.mkdirs();

            if (parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
    }


    public static void write(File file, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getAbsolutePath()), "utf-8"))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void createFile(String className, String path, String content) {
        try {
            File file = new File(path + "/" + className + ".java");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            System.out.println("Unable to create file " + className);
        }
    }

    public static String getPathFromPackageName(String packageName) {
        return packageName.replace(".", "/");
    }

}
