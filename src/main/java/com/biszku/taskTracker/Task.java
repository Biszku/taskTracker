package main.java.com.biszku.taskTracker;

import java.time.LocalDate;

public class Task {

    private String description;
    private Status status;
    private final LocalDate createdAt;
    private LocalDate updatedAt;

    public Task(String description) {
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
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

    @Override
    public String toString() {
        return ("description = %s " +
                "status = %s " +
                "created = %s " +
                "updated = %s").formatted(description, status, createdAt, updatedAt);
    }
}
