package main.java.com.biszku.taskTracker;

import java.time.LocalDate;
import java.util.StringJoiner;

public class Task {

    private final int id;
    private String description;
    private Status status;
    private final LocalDate createdAt;
    private LocalDate updatedAt;

    public Task(int id, String description) {
        this(id, description, Status.TODO, LocalDate.now(), LocalDate.now());
    }

    public Task(int id, String description, Status status,
                LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void update(String description) {
        this.description = description;
        this.updatedAt = LocalDate.now();
    }

    public void markAsInProgress() {
        this.status = Status.IN_PROGRESS;
        this.updatedAt = LocalDate.now();
    }

    public void markAsDone() {
        this.status = Status.DONE;
        this.updatedAt = LocalDate.now();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"id\":\"" + id + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"status\":\"" + status + "\"")
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }

    @Override
    public String toString() {
        return ("id = %d " +
                "description = %s " +
                "status = %s " +
                "created = %s " +
                "updated = %s").formatted(id, description, status, createdAt, updatedAt);
    }
}