package org.example;

import java.util.ArrayList;
import java.util.List;

public class ComplexTask extends Task{

    private int startHour;
    private int endHour;
    private List<Task> tasks;

    public ComplexTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.startHour = startHour;
        this.endHour = endHour;
        this.tasks = new ArrayList<>();
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public int estimateDuration() {
        int totalDuration = 0;
        for(Task task: tasks) {
            totalDuration += task.estimateDuration();
        }
        return totalDuration;
    }
}
