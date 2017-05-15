package com.ilargia.games.entitas.codeGeneration.cli.commands;


import com.ilargia.games.entitas.codeGenerator.configuration.Preferences;

import java.io.Console;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Helper {

    public static List<String> getUnusedKeys(List<String> requiredKeys, Properties properties) {
        return properties.keySet().stream()
                .filter(key -> !requiredKeys.contains(key))
                .map(String.class::cast)
                .collect(Collectors.toList());
    }

    public static List<String> getMissingKeys(List<String> requiredKeys, Properties properties) {
        return requiredKeys.stream()
                .filter(key -> !properties.containsKey(key))
                .collect(Collectors.toList());
    }

    public static boolean getUserDecision(String accept, String cancel ) {
        String key="";
        Console console = System.console();
        Scanner scanIn = new Scanner(System.in);
        do {
            key = scanIn.nextLine();

        } while(!key.equals(accept) && !key.equals(cancel));
        scanIn.close();
        return key == accept;
    }

    public static void forceAddKey(String message, String key, String value, Properties properties) {
        System.out.println(message + ": '" + key + "'");
        Console.ReadKey(true);
        properties.setProperty(key, value);
        Preferences.saveProperties(properties);
        System.out.println("Added: " + key);
    }

    public static void AddKey(String question, String key, String value, Properties properties) {
        System.out.println(question + ": '" + key + "' ? (y / n)");
        if (GetUserDecision()) {
            properties[key] = value;
            Preferences.SaveProperties(properties);
            fabl.Info("Added: " + key);
        }
    }

    public static void RemoveKey(String question, String key, Properties properties) {
        fabl.Warn(question + ": '" + key + "' ? (y / n)");
        if (GetUserDecision()) {
            properties.RemoveProperty(key);
            Preferences.SaveProperties(properties);
            fabl.Warn("Removed: " + key);
        }
    }

    public static void RemoveValue(String question, String value, List<String> values, Action<List<String>> updateAction, Properties properties) {
        fabl.Warn(question + ": '" + value + "' ? (y / n)");
        if (GetUserDecision()) {
            var valueList = values.ToList();
            valueList.Remove(value);
            updateAction(valueList.ToArray());
            Preferences.SaveProperties(properties);
            fabl.Warn("Removed: " + value);
        }
    }

    public static void AddValue(String question, String value, List<String> values, Action<List<String>> updateAction, Properties properties) {
        fabl.Info(question + ": '" + value + "' ? (y / n)");
        if (GetUserDecision()) {
            var valueList = values.ToList();
            valueList.Add(value);
            updateAction(CodeGeneratorUtil.GetOrderedNames(valueList.ToArray()));
            Preferences.SaveProperties(properties);
            fabl.Info("Added: " + value);
        }
    }
}
