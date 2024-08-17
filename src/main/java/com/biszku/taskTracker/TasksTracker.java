package main.java.com.biszku.taskTracker;

import java.util.*;

public class TasksTracker {

    private static final Scanner scanner = new Scanner(System.in);
    private static int idCounter = 0;
    private HashMap<Integer,Task> tasks;

    public TasksTracker() {
        this.tasks = new HashMap<>();
    }

    public void start() {
        while (true) {
            Stack<String> commands = getCommands();
            String operationType = commands.pop();
            switch (operationType) {
                case "ADD":
                    addTask(commands.pop());
                    break;
                case "UPDATE":
                    tasks.get(Integer.parseInt(commands.pop())).update(commands.pop());
                    break;
                case "DELETE":
                    tasks.remove(Integer.parseInt(commands.pop()));
                    break;
                case "MARK-IN-PROGRESS":
                    tasks.get(Integer.parseInt(commands.pop())).markAsInProgress();
                    break;
                case "MARK-DONE":
                    tasks.get(Integer.parseInt(commands.pop())).markAsDone();
                    break;
                case "LIST":
                    printTasks(commands);
                    break;
                case "EXIT":
                    System.exit(0);
                    return;
                default:
                    System.out.println("Unknown command");
            }
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

    private void addTask(String description) {
        Task newTask = new Task(description);
        tasks.put(++idCounter, newTask);
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
        }

    }

    public Status getTaskStatus(Map.Entry<Integer, Task> task) {
        return task.getValue().getStatus();
    }
}
