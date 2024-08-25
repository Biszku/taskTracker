package main.java.com.biszku.taskTracker;

import java.util.StringJoiner;

public class FileHandler implements Observer {

    private final String filePath;
    private final Tasks tasks;

    public FileHandler(String filePath, Tasks tasks) {
        this.filePath = filePath;
        this.tasks = tasks;
        tasks.addObserver(this);
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"file\":" + filePath)
                .toString();
    }

    @Override
    public void update() {
        System.out.println("FileHandler: " + tasks.getTasks());
    }
}