package main.java.com.biszku.taskTracker;

import java.time.LocalDate;

public class Task {
    private int id;
    private String description;
    private Status status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Task(int id) {
        this(id, "");
    }

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public void update(String description, Status status) {
        this.description = description;
        this.updatedAt = LocalDate.now();
    }
}
