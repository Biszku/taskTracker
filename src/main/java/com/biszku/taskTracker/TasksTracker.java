package main.java.com.biszku.taskTracker;

import java.util.*;
import java.util.function.Predicate;

public class TasksTracker implements Observable {

    private CommandHandler commandHandler;
    private static int idCounter;
    private final List<Task> tasks;
    private final List<Observer> observers = new ArrayList<>();

    public TasksTracker() {

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
                commandHandler = new CommandHandler();
                commandHandler.handleInput();
                boolean executed = executeCommand();
                if (!executed) break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean executeCommand() {
        String operationType = commandHandler.getCommandSlice("^(\\s\\S+).*");
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

        String description = commandHandler.getCommandSlice("^(\\s\"\\S*\")$")
                .replaceAll("\"", "");
        if (description.isEmpty()) throw commandHandler.handleIllegalArgument("Invalid description!");
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
        int id = Integer.parseInt(commandHandler.getCommandSlice("^(\\s\\d+).*"));
        int taskIndex = findTaskIndexById(id);

        String description = commandHandler.getCommandSlice("^(\\s\"\\S*\")$")
                .replaceAll("\"", "");
        if (description.isEmpty()) throw commandHandler.handleIllegalArgument("Invalid description!");
        tasks.get(taskIndex).update(description);
        notifyObservers();
    }

    private void deleteTask() {
        int id = Integer.parseInt(commandHandler.getCommandSlice("^(\\s\\d+)$"));
        int taskIndex = findTaskIndexById(id);

        tasks.remove(taskIndex);
        System.out.printf("Task deleted successfully (ID: %d)%n", id);
        notifyObservers();
    }

    private void markAsInProgress() {
        int id = Integer.parseInt(commandHandler.getCommandSlice("^(\\s\\d+)$"));
        int taskIndex = findTaskIndexById(id);

        tasks.get(taskIndex).markAsInProgress();
        notifyObservers();
    }

    private void markAsDone() {
        int id = Integer.parseInt(commandHandler.getCommandSlice("^(\\s\\d+)$"));
        int taskIndex = findTaskIndexById(id);

        tasks.get(taskIndex).markAsDone();
        notifyObservers();
    }

    private int findTaskIndexById(int id) {
            int taskIndex = Collections.binarySearch(tasks,
                    new Task(id, ""),
                    Comparator.comparingInt(Task::getId));
            if (taskIndex < 0) throw commandHandler.handleIllegalArgument("Task not found!");
            return taskIndex;
    }

    private void printTasks() {
        Status status = null;

        try {
            if (!commandHandler.isEmpty()) status = Status.valueOf(commandHandler.getCommandSlice("^(\\s\\S+)$"));
            tasks.stream().filter(getTaskPredicate(status)).forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            throw commandHandler.handleIllegalArgument("Invalid status!");
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
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}