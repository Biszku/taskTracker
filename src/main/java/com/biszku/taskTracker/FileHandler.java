package main.java.com.biszku.taskTracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileHandler implements Observer {

    private final Path filePath;
    private final Observable tasksTracker;

    public FileHandler(String fileName, Observable tasksTracker) {
        this.filePath = Path.of(fileName);
        this.tasksTracker = tasksTracker;
        this.tasksTracker.addObserver(this);
    }

    public List<Task> loadFromFile() {

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                return new ArrayList<>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<Task> tasks = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (lineContainsTask(line)) {
                    tasks.add(parseFromJSONToTask(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong during reading the file");
        }
        return tasks;
    }

    public void removeObserver() {
        tasksTracker.removeObserver(this);
    }

    private boolean lineContainsTask(String line) {
        return !Objects.equals(line, "[") && !Objects.equals(line, "]")  && !Objects.equals(line, "");
    }

    private Task parseFromJSONToTask(String line) {

        String[] plainLine = line.replace("\"","")
                .replace("}","")
                .replace("{","")
                .split(",");

        int id = Integer.parseInt(plainLine[0].split(":")[1]);
        String description = plainLine[1].split(":")[1];
        Status status = Status.fromString(plainLine[2].split(":")[1]);
        LocalDate created = LocalDate.parse(plainLine[3].split(":")[1]);
        LocalDate updated = LocalDate.parse(plainLine[4].split(":")[1]);

        return new Task(id, description, status, created, updated);
    }

    @Override
    public void update(List<Task> tasks) {
        String tasksInJSON = convertToJSON(tasks);
        saveToFile(tasksInJSON);
    }

    private String convertToJSON(List<Task> tasks) {
        String delimiter = "," + System.lineSeparator();
        return tasks.stream()
                .map(Task::toJSON)
                .collect(Collectors.joining(delimiter, "[\n", "\n]"));
    }

    private void saveToFile(String tasksInJSON) {

        try {
            Files.writeString(filePath, tasksInJSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}