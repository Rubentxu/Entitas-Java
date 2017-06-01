package ilargia.entitas.codeGeneration.cli;

import java.util.List;

public class ArgsExtension {

    public static boolean isForce(List<String> args) {
        return args.stream().anyMatch(arg -> arg == "-f");
    }

    public static boolean isVerbose(List<String> args) {
        return args.stream().anyMatch(arg -> arg == "-v");
    }

    public static boolean isSilent(List<String> args) {
        return args.stream().anyMatch(arg -> arg == "-s");
    }
}
