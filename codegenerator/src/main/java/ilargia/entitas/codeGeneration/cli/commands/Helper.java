package ilargia.entitas.codeGeneration.cli.commands;


import ilargia.entitas.codeGeneration.CodeGeneratorUtil;

import java.io.Console;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Helper {
    public static List<String> getUnusedKeys(Set<Object> requiredKeys, Properties properties) {
        return properties.keySet().stream()
                .filter(key -> !requiredKeys.contains(key))
                .map(String.class::cast)
                .collect(Collectors.toList());
    }

    public static List<String> getMissingKeys(Set<Object> requiredKeys, Properties properties) {
        return requiredKeys.stream()
                .filter(key -> !properties.containsKey(key))
                .map(p -> (String) p)
                .collect(Collectors.toList());
    }

    public static boolean getUserDecision(String accept, String cancel) {
        String key = "";
        Console console = System.console();
        Scanner scanIn = new Scanner(System.in);
        do {
            key = scanIn.nextLine();

        } while (!key.equals(accept) && !key.equals(cancel));
        scanIn.close();
        return key == accept;
    }

    public static void forceAddKey(String message, String key, String value, Properties properties) {
        System.out.println(message + ": '" + key + "'");
        //Console.ReadKey(true);
        properties.setProperty(key, value);
      //  Preferences.saveProperties(properties);
        System.out.println("Added: " + key);
    }

    public static void addKey(String question, String key, String value, Properties properties) {
        System.out.println(question + ": '" + key + "' ? (y / n)");
        if (getUserDecision("yes", "no")) {
            properties.setProperty(key, value);
     //       Preferences.saveProperties(properties);
            System.out.println("Added: " + key);
        }
    }

    public static void removeKey(String question, String key, Properties properties) {
        System.out.println(question + ": '" + key + "' ? (y / n)");
        if (getUserDecision("yes", "no")) {
            properties.remove(key);
      //      Preferences.saveProperties(properties);
            System.out.println("Removed: " + key);
        }
    }

    public static void removeValue(String question, String value, List<String> valueList, Action<List<String>> updateAction, Properties properties) {
        System.out.println(question + ": '" + value + "' ? (y / n)");
        if (getUserDecision("yes", "no")) {
            valueList.remove(value);
            updateAction.apply(valueList);
      //      Preferences.saveProperties(properties);
            System.out.println("Removed: " + value);
        }
    }

    public static void addValue(String question, String value, List<String> valueList, Action<List<String>> updateAction, Properties properties) {
        System.out.println(question + ": '" + value + "' ? (y / n)");
        if (getUserDecision("yes", "no")) {
            valueList.add(value);
            updateAction.apply(CodeGeneratorUtil.getOrderedNames(valueList));
        //    Preferences.saveProperties(properties);
            System.out.println("Added: " + value);
        }
    }

    @FunctionalInterface
    public interface Action<T> {
        void apply(T param);

    }

}
