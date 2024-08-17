package main.java.com.biszku.taskTracker;

import java.util.*;

public class TasksTracker {

    private static final Scanner scanner = new Scanner(System.in);
    private static int idCounter = 0;
    private final HashMap<Integer,Task> tasks = new HashMap<>();

    public static void run() {
        TasksTracker tasksTracker = new TasksTracker();
        tasksTracker.menuController();
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
        Task newTask = new Task(description);
        tasks.put(++idCounter, newTask);
    }

    private void updateTask(Stack<String> commands) {
        int taskId = Integer.parseInt(commands.pop());
        String description = commands.pop();
        tasks.get(taskId).update(description);
    }

    private void deleteTask(Stack<String> commands) {
        int taskId = Integer.parseInt(commands.pop());
        tasks.remove(taskId);
    }

    private void markAsInProgress(Stack<String> commands) {
        int taskId = Integer.parseInt(commands.pop());
        tasks.get(taskId).markAsInProgress();
    }

    private void markAsDone(Stack<String> commands) {
        int taskId = Integer.parseInt(commands.pop());
        tasks.get(taskId).markAsDone();
    }

    private void printTasks(Stack<String> commands) {

        try {
            Status status = Status.valueOf(commands.pop());
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                if (getTaskStatus(entry).equals(status)) {
                    System.out.println("id = " + entry.getKey() + " " + entry.getValue());
                }
            }
        } catch (EmptyStackException e) {
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                System.out.println("id = " + entry.getKey() + " " + entry.getValue());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown status");
        }
    }

    public Status getTaskStatus(Map.Entry<Integer, Task> task) {
        return task.getValue().getStatus();
    }
}