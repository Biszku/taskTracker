package main.java.com.biszku.taskTracker;

import java.util.*;
import java.util.function.Predicate;

public class TasksTracker implements Observable  {

    private static int idCounter;
    private final List<Task> tasks;
    private final List<Observer> observers;

    public TasksTracker() {
        observers = new ArrayList<>();
        tasks = new ArrayList<>();

        FileHandler fileHandler = new FileHandler("tasks.json", this);
        fileHandler.loadFromFile();
    }

    public void run() {

        while (true) {
            try {
                Stack<String> commands = createCommandStack();
                boolean executed = executeCommand(commands);
                if (!executed) break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setIdCounter(int idCounter) {
        TasksTracker.idCounter = idCounter;
    }

    private Stack<String> createCommandStack() {
        Stack<String> commandsStack = new Stack<>();
        String[] commandStructure = readInput().toLowerCase().split(" ");

        try {
            commandStructure[1] = commandStructure[1].toUpperCase();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw createIllegalArgumentException("Invalid syntax!\n" +
                    "Enter command in format \"task-cli <operationType>\"");
        }

        for (int i = commandStructure.length - 1; i >= 0; i--) {
            commandsStack.push(commandStructure[i]);
        }

        if (!Objects.equals(commandsStack.pop(), "task-cli")) {
            throw createIllegalArgumentException("Invalid syntax!\n" +
                    "Enter command in format \"task-cli <operationType>\"");
        }

        return commandsStack;
    }

    private String readInput() {
        return new Scanner(System.in).nextLine();
    }

    private IllegalArgumentException createIllegalArgumentException(String message) {
        return new IllegalArgumentException("\u001B[31m" + message + "\u001B[0m");
    }

    private IllegalArgumentException createEmptyStackException(String message) {
        return new IllegalArgumentException("\u001B[31mInvalid syntax!\n" +
                "Enter arguments in format: " + message + "\u001B[0m");
    }

    private boolean executeCommand(Stack<String> commands) {

        String operationType = commands.pop();
        switch (operationType) {
            case "ADD":
                addTask(commands);
                break;
            case "UPDATE":
                updateTask(commands);
                break;
            case "DELETE":
                deleteTask(commands);
                break;
            case "MARK-IN-PROGRESS":
                markAsInProgress(commands);
                break;
            case "MARK-DONE":
                markAsDone(commands);
                break;
            case "LIST":
                printTasks(commands);
                break;
            case "EXIT":
                return false;
            default:
                System.out.println("\u001B[31m" +
                        "Unknown command!\n" +
                        "Enter one of the following commands\n" +
                        "add, update, delete, mark-in-progress, mark-done, list, exit" +
                        "\u001B[0m");
        }
        return true;
    }

    private void addTask(Stack<String> commands) {

        try {
            String description = commands.pop();
            Task newTask = new Task(++idCounter, description);
            tasks.add(newTask);
        } catch (EmptyStackException e) {
            throw createEmptyStackException("add <description>");
        }

        notifyObservers();
    }

    private void updateTask(Stack<String> commands) {
        int taskIndex = findTaskIndexById(commands);
        String description = commands.pop();
        tasks.get(taskIndex).update(description);
        notifyObservers();
    }

    private void deleteTask(Stack<String> commands) {
        int taskIndex = findTaskIndexById(commands);
        tasks.remove(taskIndex);
        notifyObservers();
    }

    private void markAsInProgress(Stack<String> commands) {
        int taskIndex = findTaskIndexById(commands);
        tasks.get(taskIndex).markAsInProgress();
        notifyObservers();
    }

    private void markAsDone(Stack<String> commands) {
        int taskIndex = findTaskIndexById(commands);
        tasks.get(taskIndex).markAsDone();
        notifyObservers();
    }

    private int findTaskIndexById(Stack<String> commands) {
        int taskId = Integer.parseInt(commands.pop());
        return findTaskIndexById(taskId);
    }

    private int findTaskIndexById(int taskId) {
        int startIndex = 0;
        int endIndex = tasks.size() - 1;

        while (startIndex <= endIndex) {
            int middleIndex = (startIndex + endIndex) / 2;
            if (tasks.get(middleIndex).getId() == taskId) {
                return middleIndex;
            } else if (tasks.get(middleIndex).getId() < taskId) {
                startIndex = middleIndex + 1;
            } else {
                endIndex = middleIndex - 1;
            }
        }

        return -1;
    }

    private void printTasks(Stack<String> commands) {

        Status status = null;

        try {
            if (!commands.isEmpty())
                status = Status.valueOf(commands.pop());
            tasks.stream().filter(getTaskPredicate(status)).forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown status");
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