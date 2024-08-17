package main.java.com.biszku.taskTracker;

import java.util.*;

public class TasksTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static int idCounter = 0;
    private List<Task> tasks;

    public TasksTracker() {
        this.tasks = new ArrayList<>();
    }

    public void start() {
        while (true) {
            Stack<String> commands = getCommands();
            String operationType = commands.pop();
            switch (operationType) {
                case "ADD":
                    break;
                case "UPDATE":
                    break;
                case "DELETE":
                    break;
                case "MARK-IN-PROGRESS":
                    break;
                case "MARK-DONE":
                    break;
                case "LIST":
                    break;
                case "EXIT":
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

    private void addTask(Task task) {
        Task newTask = new Task(idCounter++);
        tasks.add(newTask);
    }
}
