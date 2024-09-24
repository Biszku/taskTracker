package main.java.com.biszku.taskTracker;

import java.util.List;

public interface Observer {
    void update(List<Task> tasks);
}