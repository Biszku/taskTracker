package main.java.com.biszku.taskTracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileHandler implements Observer {

    private final String filePath;
    private final TasksTracker tasks;

    public FileHandler(String filePath, TasksTracker tasks) {
        this.filePath = filePath;
        this.tasks = tasks;
        tasks.addObserver(this);
    }

    public void loadFromFile() {
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!Objects.equals(line, "[") && !Objects.equals(line, "]")){
                    parseFromJSONToTask(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong during reading the file");
        }
    }

    private void parseFromJSONToTask(String line) {
        String[] plainLine = line.replace("\"","")
                .replace("}","")
                .replace("{","")
                .split(",");

        int id = Integer.parseInt(plainLine[0].split(":")[1]);
        String description = plainLine[1].split(":")[1];
        Status status = Status.valueOf(plainLine[2].split(":")[1]);
        LocalDate createdAt = LocalDate.parse(plainLine[3].split(":")[1]);
        LocalDate updatedAt = LocalDate.parse(plainLine[4].split(":")[1]);
        tasks.setIdCounter(id);
        tasks.getTasks().add(new Task(id, description, status, createdAt, updatedAt));
    }

    @Override
    public void update() {
        String tasksInJSON = convertToJSON();
        saveToFile(tasksInJSON);
    }

    private String convertToJSON() {
        String delimiter = "," + System.lineSeparator();
        return tasks.getTasks().stream()
                .map(Task::toJSON)
                .collect(Collectors.joining(delimiter, "[\n", "\n]"));
    }

    private void saveToFile(String tasksInJSON) {
        try {
            Files.writeString(Path.of(filePath), tasksInJSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}