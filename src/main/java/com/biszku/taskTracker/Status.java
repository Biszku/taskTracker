package main.java.com.biszku.taskTracker;

public enum Status {
    TODO, IN_PROGRESS, DONE;

    @Override
    public String toString() {
        return switch (this) {
            case TODO -> "TODO";
            case IN_PROGRESS -> "IN_PROGRESS";
            case DONE -> "DONE";
        };
    }
}
