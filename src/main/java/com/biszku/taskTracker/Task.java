package main.java.com.biszku.taskTracker;

import java.time.LocalDate;
import java.util.StringJoiner;

public class Task {

    private final int id;
    private String description;
    private Status status;
    private final LocalDate created;
    private LocalDate updated;

    public Task(int id, String description) {

        this(id, description, Status.TODO, LocalDate.now(), LocalDate.now());
    }

    public Task(int id, String description, Status status,
                LocalDate created, LocalDate updated) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void update(String description) {
        this.description = description;
        this.updated = LocalDate.now();
        System.out.printf("Task updated successfully (ID: %d)%n", id);
    }

    public void markAsInProgress() {
        this.status = Status.IN_PROGRESS;
        this.updated = LocalDate.now();
        System.out.printf("Task marked as in-progress (ID: %d)%n", id);
    }

    public void markAsDone() {
        this.status = Status.DONE;
        this.updated = LocalDate.now();
        System.out.printf("Task marked as done (ID: %d)%n", id);
    }

    public String toJSON() {

        return new StringJoiner(", ", "{", "}")
                .add("\"id\":\"" + id + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"status\":\"" + status + "\"")
                .add("\"created\":\"" + created + "\"")
                .add("\"updated\":\"" + updated + "\"")
                .toString();
    }

    @Override
    public String toString() {

        return ("ID: %d, " +
                "Description: %s, " +
                "Status: %s, " +
                "Created: %s, " +
                "Updated: %s").formatted(id, description, status, created, updated);
    }
}