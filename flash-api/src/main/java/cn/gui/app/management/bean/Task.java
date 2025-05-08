package cn.gui.app.management.bean;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String taskName;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalMinutes;
    private String status;

    public Task() {}

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.status = "NOT_STARTED";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public int getTotalMinutes() { return totalMinutes; }
    public void setTotalMinutes(int totalMinutes) { this.totalMinutes = totalMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return taskName + " (" + status + ")";
    }
}