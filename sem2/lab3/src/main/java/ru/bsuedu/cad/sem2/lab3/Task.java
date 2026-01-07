package ru.bsuedu.cad.sem2.lab3;

import java.time.LocalDateTime;

public class Task {
    private static Long nextId = 1L;
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime deadLine = LocalDateTime.MAX;
    private boolean completed;

    // Constructors
    public Task() {
        this.id = generateId();
    }

    public Task(String title, String description, String category) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        this.category = category;
        this.completed = false;
    }

    public Task(String title, String description, String category, LocalDateTime deadLine) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.deadLine = deadLine;
    }

    private synchronized Long generateId() {
        return nextId++;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadline) {
        this.deadLine = deadline;
    }

    @Override
    public String toString() {
        return "Task " + 
               "[id=" + id + 
               ", title=" + title + 
               ", description=" + description + 
               ", category=" + category + 
               ", completed=" + completed + 
               "]";
    }
}