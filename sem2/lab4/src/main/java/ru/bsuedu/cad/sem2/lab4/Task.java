package ru.bsuedu.cad.sem2.lab4;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
    // private static Long nextId = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private LocalDateTime deadLine;
    private boolean completed = false;

    @ManyToOne
    private User author;

    // Constructors
    public Task() {
        // this.id = generateId();
    }

    public Task(String title, String description, String category) {
        // this.id = generateId();
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public Task(String title, String description, String category, LocalDateTime deadLine) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.deadLine = deadLine;
    }

    public Task(String title, String description, String category, LocalDateTime deadLine, User author) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.deadLine = deadLine;
        this.author = author;
        this.completed = false;
    }

    /*
    private synchronized Long generateId() {
        return nextId++;
    }
    */

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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