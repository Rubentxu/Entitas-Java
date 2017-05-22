package ilargia.entitas.codeGeneration.cli;

import ilargia.entitas.codeGeneration.interfaces.ICommand;
import ilargia.entitas.codeGeneration.cli.commands.*;

import java.util.*;
import java.util.stream.Collectors;

public class Program {

    private static List<ICommand> getCommands() {
        return new ArrayList<ICommand>() {{
            add(new Doctor());
            add(new DryRun());
            add(new EditConfig());
            add(new FixConfig());
            add(new Generate());
            add(new NewConfig());
            add(new Status());

        }};
    }

    public static void main(String[] args) {
        List<ICommand> commands = getCommands().stream()
                .sorted((a, b) -> a.trigger().compareTo(b.trigger()))
                .collect(Collectors.toList());

        if (args == null || args.length == 0) {
            printUsage(commands);
            return;
        }

        setupLogging(Arrays.asList(args));

        try {
            getCommand(commands, args[0]).run(args);
        } catch (Exception ex) {
            printException(ex, Arrays.asList(args));
        }
    }

    static ICommand getCommand(List<ICommand> commands, String trigger) throws Exception {
        Optional<ICommand> command = commands.stream()
                .filter(c -> c.trigger() == trigger)
                .findFirst();
        if (!command.isPresent()) {
            throw new Exception("command not found: " + trigger);
        }
        return command.get();
    }

    static void printException(Exception ex, List<String> args) {

        if (ArgsExtension.isVerbose(args)) {
            System.out.println(ex.toString());
        } else {
            System.out.println(ex.getMessage());
        }

    }

    static void printUsage(List<ICommand> commands) {
        int pad = commands.stream()
                .max(Comparator.comparingInt(value -> value.example().length()))
                .map(o -> o.example().length())
                .get();
        List<String> commandList = commands.stream()
                .map(c -> c.example() + " - " + c.description())
                .collect(Collectors.toList());

        commandList.add(String.format("%1$-" + pad + "s", "[-v]", " - verbose output"));
        commandList.add(String.format("%1$-" + pad + "s", "[-s]", " - silent output (errors only)"));


        System.out.println("Entitas Code Generator version 1");
        System.out.println(String.format("usage:\n{0}", String.join("\n", commandList)));
    }

    static void setupLogging(List<String> args) {
//        if (isVerbose(args)) {
//            fabl.globalLogLevel = LogLevel.On;
//        } else if (args.isSilent()) {
//            fabl.globalLogLevel = LogLevel.Error;
//        } else {
//            fabl.globalLogLevel = LogLevel.Info;
//        }
//
//        fabl.AddAppender((logger, logLevel, message) =>{
//            if (_consoleColors.ContainsKey(logLevel)) {
//                Console.ForegroundColor = _consoleColors[logLevel];
//                Console.WriteLine(message);
//                Console.ResetColor();
//            } else {
//                Console.WriteLine(message);
//            }
//        });
    }


}