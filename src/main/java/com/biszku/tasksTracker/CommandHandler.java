package main.java.com.biszku.tasksTracker;

import java.util.List;

public class CommandHandler {

    private final int ACTION_POSITION = 0;
    private final int DESCRIPTION_POSITION = 1;
    private final int ID_POSITION = 1;

    private final int STATUS_POSITION = 1;
    private final int NEW_DESCRIPTION_POSITION = 2;

    public CommandHandler() {
    }

    public String getActionType(List<String> command) {
        try {
            return command.get(ACTION_POSITION);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    public String getDescription(List<String> command) {
        try {
            return command.get(DESCRIPTION_POSITION);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Description is missing");
        }
    }

    public String getNewDescription(List<String> command) {
        try {
            return command.get(NEW_DESCRIPTION_POSITION);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Description is missing");
        }
    }

    public int getId(List<String> command) {
        try {
            return Integer.parseInt(command.get(ID_POSITION));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Id is missing");
        }
    }

    public Status getStatus(List<String> command) {
        try {
            return Status.fromString(command.get(STATUS_POSITION));
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}