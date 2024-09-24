package main.java.com.biszku.taskTracker;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    private String command;

    public void readCommand() {
        command = readInput();
        String transformedCommand = getCommandSlice("^(task-cli).*");
        if (transformedCommand.isEmpty()) throw handleIllegalArgument("\u001B[31m" +
                "Invalid syntax!\n" +
                "Enter command in format \"task-cli <operationType> <arguments>\"" +
                "\u001B[0m");
    }

    public String getCommandSlice(String regex) {
        String prefix = "";
        if (getMatcher(regex).matches()) {
            prefix = getArgument(regex);
            command = command.replace(prefix, "");
            return prefix.strip();
        }
        return prefix;
    }

    public IllegalArgumentException handleIllegalArgument(String message) {
        return new IllegalArgumentException("\u001B[31m" + message + "\u001B[0m");
    }

    public boolean isEmpty() {
        return command.isEmpty();
    }

    private String readInput() {
        return new Scanner(System.in).nextLine();
    }

    private Matcher getMatcher(String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }

    private String getArgument(String regex) {
        Matcher matcher = getMatcher(regex);
        String argument = "";
        if (matcher.find()) argument = matcher.group(1);
        return argument;
    }
}