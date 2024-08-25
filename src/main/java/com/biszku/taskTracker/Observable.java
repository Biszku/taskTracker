package main.java.com.biszku.taskTracker;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}