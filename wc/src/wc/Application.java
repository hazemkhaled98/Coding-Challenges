package wc;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        try {
            CommandLineArgs parsedArgs = parseArgs(args);
            CommandInvoker.execute(parsedArgs.command, parsedArgs.source);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            System.exit(1);
        }
    }

    private static CommandLineArgs parseArgs(String[] args) {
        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException("Invalid number of arguments.");
        }

        String command = "-all";
        String source = "Stdin";

        if (args.length == 1) {
            if (args[0].startsWith("-")) {
                command = args[0];
            } else {
                source = args[0];
            }
        } else {
            command = args[0];
            source = args[1];
        }

        return new CommandLineArgs(Command.fromString(command), source);
    }

    private static void printUsage() {
        System.out.println("Usage: wc <command> <source>");
        System.out.println("Commands: " + Arrays.toString(Command.values()));
        System.out.println("Source: file path or 'Stdin' (default)");
    }

    private record CommandLineArgs(Command command, String source) {}
}