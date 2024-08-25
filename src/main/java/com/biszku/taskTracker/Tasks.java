package main.java.com.biszku.taskTracker;

import java.util.*;
import java.util.function.Predicate;

public class Tasks implements Observable {

    private static final Scanner scanner = new Scanner(System.in);
    private static int idCounter = 0;
    private final List<Observer> observers = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>(40);

    public static void run() {
        Tasks tasksTracker = new Tasks();
        new FileHandler("tasks.json", tasksTracker);
        tasksTracker.menuController();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    private void menuController() {

        while (true) {
            Stack<String> commands = getCommands();
            boolean executed = executeCommand(commands);
            if (!executed) break;
        }
    }

    private Stack<String> getCommands() {

        Stack<String> commandsStack = new Stack<>();
        String[] commandStructure = scanner.nextLine().toUpperCase().split(" ");
        for (int i = commandStructure.length - 1; i >= 0; i--) {
            commandsStack.push(commandStructure[i]);
        }
        return commandsStack;
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
                System.out.println("Unknown command");
        }
        return true;
    }

    private void addTask(Stack<String> commands) {
        String description = commands.pop();
        Task newTask = new Task(++idCounter, description);
        tasks.add(newTask);
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