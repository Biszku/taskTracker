package main.java.com.biszku.tasksTracker;

import java.util.*;

public class TasksTracker {
    private final Tasks tasks;
    private final CommandHandler commandHandler;
    private List<String> command;

    public static void main(String[] command) {

        TasksTracker tasksTracker = new TasksTracker();
        tasksTracker.execute(command);
    }

    public TasksTracker() {
        commandHandler = new CommandHandler();
        tasks = new Tasks();
    }

    public void execute(String[] command) {
        this.command = Arrays.asList(command);

        try {
            handleAction();
        } catch (IllegalArgumentException e) {
            System.out.println(renderError(e.getMessage()));
        }
    }

    private void handleAction() {
        String actionTypeType = commandHandler.getActionType(this.command);

        switch (actionTypeType) {
            case "add" -> addTask();
            case "update" -> updateTask();
            case "delete" -> deleteTask();
            case "mark-in-progress" -> markAsInProgress();
            case "mark-done" -> markAsDone();
            case "list" -> printTasks();
            default -> throw new IllegalArgumentException(renderOperationTypeError());
        }
    }

    private String renderError(String message) {
        return "\u001B[31mERROR! " + message + "\u001B[0m";
    }

    private String renderOperationTypeError() {
        return """
                Unknown command!
                Enter one of the following commands
                add, update, delete, mark-in-progress, mark-done, list, exit
                """;
    }

    private void addTask() {
        String description = commandHandler.getDescription(this.command);
        tasks.createAndAddTask(description);
    }

    private void updateTask() {
        int id = commandHandler.getId(this.command);
        String newDescription = commandHandler.getNewDescription(this.command);
        tasks.update(id, newDescription);
    }

    private void deleteTask() {
        int id = commandHandler.getId(this.command);
        tasks.remove(id);
    }

    private void markAsInProgress() {
        int id = commandHandler.getId(this.command);
        tasks.markAsInProgress(id);
    }

    private void markAsDone() {
        int id = commandHandler.getId(this.command);
        tasks.markAsDone(id);
    }

    private void printTasks() {
        Status status = commandHandler.getStatus(this.command);
        tasks.printTasks(status);
    }
}