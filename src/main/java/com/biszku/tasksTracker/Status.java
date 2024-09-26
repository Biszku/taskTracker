package main.java.com.biszku.tasksTracker;

public enum Status {
    TODO("to-do"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}