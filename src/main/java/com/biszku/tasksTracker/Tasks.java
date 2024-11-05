package main.java.com.biszku.tasksTracker;

import java.util.List;
import java.util.function.Predicate;

public class Tasks {
    private final List<Task> tasks;
    private final Storage storage;
    private static int idCounter;

    public Tasks() {
        this.storage = new Storage("tasks.json");
        this.tasks = storage.loadFromFile();

        Tasks.idCounter = !tasks.isEmpty() ? tasks.get(tasks.size() - 1).getId() : 0;
    }

    public void createAndAddTask(String description) {
        Task task = new Task(++idCounter, description);
        tasks.add(task);
        storage.saveToFile(storage.convertToJSON(tasks));
        System.out.printf("Task added successfully (ID: %d)%n", task.getId());
    }

    public void update(int id, String newDescription) {
        int taskIndex = findTaskIndexById(id);
        tasks.get(taskIndex).update(newDescription);
        storage.saveToFile(storage.convertToJSON(tasks));
        System.out.printf("Task updated successfully (ID: %d)%n", id);
    }

    public void remove(int id) {
        int taskIndex = findTaskIndexById(id);
        tasks.remove(taskIndex);
        storage.saveToFile(storage.convertToJSON(tasks));
        System.out.printf("Task deleted successfully (ID: %d)%n", id);
    }

    public void markAsInProgress(int id) {
        int taskIndex = findTaskIndexById(id);
        tasks.get(taskIndex).markAsInProgress();
        storage.saveToFile(storage.convertToJSON(tasks));
        System.out.printf("Task marked as in-progress (ID: %d)%n", id);
    }

    public void markAsDone(int id) {
        int taskIndex = findTaskIndexById(id);
        tasks.get(taskIndex).markAsDone();
        storage.saveToFile(storage.convertToJSON(tasks));
        System.out.printf("Task marked as done (ID: %d)%n", id);
    }

    public void printTasks(Status status) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found!");
        } else {
            tasks.stream().filter(getTaskPredicate(status)).forEach(System.out::println);
        }
    }

    private int findTaskIndexById(int id) {

        int firstIndex = 0;
        int lastIndex = tasks.size() - 1;
        int middleIndex;

        while (firstIndex <= lastIndex) {
            middleIndex = (firstIndex + lastIndex) / 2;
            if (tasks.get(middleIndex).getId() == id) {
                return middleIndex;
            } else if (tasks.get(middleIndex).getId() < id) {
                firstIndex = middleIndex + 1;
            } else {
                lastIndex = middleIndex - 1;
            }
        }

        throw new IllegalArgumentException("Task not found!");
    }

    private Predicate<Task> getTaskPredicate(Status status) {
        return status == null ? task -> true : task -> task.getStatus().equals(status);
    }
}
