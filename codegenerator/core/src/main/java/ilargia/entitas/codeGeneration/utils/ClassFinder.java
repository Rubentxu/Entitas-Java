package ilargia.entitas.codeGeneration.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String JAR_FILE_SUFFIX = ".jar!";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    private static final String BAD_FILE_ERROR = "Unable to get resources from path '%s'. Are you sure the class file '%s' exists?";

    public static Class<?> findClass(String className) {
        int endIndex = className.length() - CLASS_FILE_SUFFIX.length();
        String finalClassName = className.substring(0, endIndex);
        String scannedClass = finalClassName.replace(PKG_SEPARATOR, DIR_SEPARATOR) + CLASS_FILE_SUFFIX;

        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedClass);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_FILE_ERROR, scannedClass, finalClassName));
        }
        File scannedFile = new File(scannedUrl.getFile());

        if (scannedFile.exists()) {
            try {
                return Class.forName(finalClassName);
            } catch (ClassNotFoundException e) {
                System.out.println(String.format(BAD_FILE_ERROR, scannedClass, finalClassName));
            }

        } else {
            return findIntoPackage(scannedUrl.getFile()
                    .substring(5, scannedUrl.getFile().lastIndexOf(JAR_FILE_SUFFIX) + 4), finalClassName).stream().findFirst().get();
        }
        return null;
    }

    public static List<Class<?>> findRecursive(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());

        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (scannedDir.exists()) {
            for (File file : scannedDir.listFiles()) {
                classes.addAll(find(file, scannedPackage));
            }
        } else {
            classes.addAll(findIntoPackage(scannedUrl.getFile()
                            .substring(5, scannedUrl.getFile().lastIndexOf(JAR_FILE_SUFFIX) + 4) , scannedPackage));
        }
        return classes;
    }

    public static List<Class<?>> findIntoPackage(String pathJar, String scannedPackage) {
        List<Class<?>> classNames = new ArrayList<>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(pathJar));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(CLASS_FILE_SUFFIX) && !entry.getName().contains("$")) {
                    if(scannedPackage.contains(CLASS_FILE_SUFFIX)) {
                        int endIndex = scannedPackage.length() - CLASS_FILE_SUFFIX.length();
                        scannedPackage = scannedPackage.substring(0, endIndex);
                    }

                    String scannedClass = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
                    if(entry.getName().contains(scannedClass)) {
                        // This ZipEntry represents a class. Now, what class does it represent?
                        String className = entry.getName().replace('/', '.'); // including ".class"
                        classNames.add(Class.forName(className.substring(0, className.length() - CLASS_FILE_SUFFIX.length())));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return classNames;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
//
//    private static Class<?> findOneClass(File file, String scannedPackage) {
//        List<Class<?>> classes = new ArrayList<Class<?>>();
//        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
//        if (!file.isDirectory()) {
//            for (File child : file.listFiles()) {
//                classes.addAll(find(child, resource));
//            }
//        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
//            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
//            String className = resource.substring(0, endIndex);
//            try {
//                classes.add(Class.forName(className));
//            } catch (ClassNotFoundException ignore) {
//            }
//        }
//        return classes;
//    }

}