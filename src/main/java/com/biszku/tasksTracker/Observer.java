package main.java.com.biszku.tasksTracker;

import java.util.List;

public interface Observer {
    void update(List<Task> tasks);
}