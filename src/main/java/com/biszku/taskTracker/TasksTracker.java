package main.java.com.biszku.taskTracker;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TasksTracker implements Observable {

    private String command;
    private static int idCounter;
    private final List<Task> tasks;
    private final List<Observer> observers;

    public TasksTracker() {
        observers = new ArrayList<>();
        FileHandler fileHandler = new FileHandler("tasks.json", this);
        tasks = fileHandler.loadFromFile();
        idCounter = tasks.isEmpty() ? 0 : tasks.get(tasks.size() - 1).getId();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void run() {

        while (true) {
            try {
                handleInput();
                boolean executed = executeCommand();
                if (!executed) break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleInput() {
        command = readInput();
        String transformedCommand = transformCommand("^(task-cli).*");
        if (transformedCommand.isEmpty()) throw createIllegalArgumentException("\u001B[31m" +
                "Invalid syntax!\n" +
                "Enter command in format \"task-cli <operationType> <arguments>\"" +
                "\u001B[0m");
    }

    private String transformCommand(String regex) {
        String prefix = "";
        if (getMatcher(regex).matches()) {
            prefix = getArgument(regex);
            command = command.replace(prefix, "");
            return prefix;
        }
        return prefix;
    }

    private String getArgument(String regex) {
        Matcher matcher = getMatcher(regex);
        String argument = "";
        if (matcher.find()) argument = matcher.group(1);
        return argument;
    }

    private Matcher getMatcher(String regex) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        return matcher;
    }

    private IllegalArgumentException createIllegalArgumentException(String message) {
        return new IllegalArgumentException("\u001B[31m" + message + "\u001B[0m");
    }

    private String readInput() {
        return new Scanner(System.in).nextLine();
    }

    private boolean executeCommand() {
        String operationType = transformCommand("^(\\s\\S+).*").strip();
        switch (operationType) {
            case "add" -> addTask();
            case "update" -> updateTask();
            case "delete" -> deleteTask();
            case "mark-in-progress" -> markAsInProgress();
            case "mark-done" -> markAsDone();
            case "list" -> printTasks();
            case "exit" -> {
                System.out.println("Closing application...");
                return false;
            }
            default -> System.out.println("\u001B[31m" +
                        "Unknown command!\n" +
                        "Enter one of the following commands\n" +
                        "add, update, delete, mark-in-progress, mark-done, list, exit" +
                        "\u001B[0m");
        }
        return true;
    }

    private void addTask() {

        String description = transformCommand("^(\\s\"\\S*\")$").strip()
                .replaceAll("\"", "");
        if (description.isEmpty()) throw createIllegalArgumentException("Invalid description!");
        Task newTask = new Task(++idCounter, description);
        tasks.add(newTask);
        System.out.printf("Task added successfully (ID: %d)%n", idCounter);
        notifyObservers();
    }

    private IllegalArgumentException createEmptyStackException(String message) {
        return new IllegalArgumentException("\u001B[31mInvalid syntax!\n" +
                "Enter arguments in format: " + message + "\u001B[0m");
    }

    private void updateTask() {
        int id = Integer.parseInt(transformCommand("^(\\s\\d+).*").strip());
        int taskIndex = findTaskIndexById(id);

        String description = transformCommand("^(\\s\"\\S*\")$").strip()
                .replaceAll("\"", "");
        tasks.get(taskIndex).update(description);
        notifyObservers();
    }

    private void deleteTask() {
        int id = Integer.parseInt(transformCommand("^(\\s\\d+)$").strip());
        int taskIndex = findTaskIndexById(id);

        tasks.remove(taskIndex);
        System.out.printf("Task deleted successfully (ID: %d)%n", id);
        notifyObservers();
    }

    private void markAsInProgress() {
        int id = Integer.parseInt(transformCommand("^(\\s\\d+)$").strip());
        int taskIndex = findTaskIndexById(id);

        tasks.get(taskIndex).markAsInProgress();
        notifyObservers();
    }

    private void markAsDone() {
        int id = Integer.parseInt(transformCommand("^(\\s\\d+)$").strip());
        int taskIndex = findTaskIndexById(id);

        tasks.get(taskIndex).markAsDone();
        notifyObservers();
    }

    private int findTaskIndexById(int id) {
            int taskIndex = Collections.binarySearch(tasks,
                    new Task(id, ""),
                    Comparator.comparingInt(Task::getId));
            if (taskIndex < 0) throw createIllegalArgumentException("Task not found!");
            return taskIndex;
    }

    private void printTasks() {
        Status status = null;

        try {
            if (!command.isEmpty()) status = Status.valueOf(transformCommand("^(\\s\\S+)$").strip());
            tasks.stream().filter(getTaskPredicate(status)).forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            throw createIllegalArgumentException("Invalid status!");
        }
    }

    private Predicate<Task> getTaskPredicate(Status status) {
        return status == null ? task -> true : task -> task.getStatus().equals(status);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}