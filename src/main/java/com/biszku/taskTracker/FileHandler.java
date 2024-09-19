package main.java.com.biszku.taskTracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileHandler implements Observer {

    private final Path filePath;
    private final TasksTracker tasksTracker;

    public FileHandler(String fileName, TasksTracker tasksTracker) {
        this.filePath = Paths.get(fileName);
        this.tasksTracker = tasksTracker;
        tasksTracker.addObserver(this);
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

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!Objects.equals(line, "[") && !Objects.equals(line, "]")){
                    tasks.add(parseFromJSONToTask(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong during reading the file");
        }
        return tasks;
    }

    private Task parseFromJSONToTask(String line) {
        String[] plainLine = line.replace("\"","")
                .replace("}","")
                .replace("{","")
                .split(",");

        int id = Integer.parseInt(plainLine[0].split(":")[1]);
        String description = plainLine[1].split(":")[1];
        Status status = Status.valueOf(plainLine[2].split(":")[1]);
        LocalDate createdAt = LocalDate.parse(plainLine[3].split(":")[1]);
        LocalDate updatedAt = LocalDate.parse(plainLine[4].split(":")[1]);

        return new Task(id, description, status, createdAt, updatedAt);
    }

    @Override
    public void update() {
        String tasksInJSON = convertToJSON();
        saveToFile(tasksInJSON);
    }

    private String convertToJSON() {
        String delimiter = "," + System.lineSeparator();
        return tasksTracker.getTasks().stream()
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