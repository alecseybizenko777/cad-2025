package ru.bsuedu.cad.sem2.lab3;

import java.util.List;

import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class TaskRepository {

    private List<Task> taskList = new LinkedList<>();

    public void addTask(Task task) {
        taskList.add(task);
    }

    public Task getTask(Long id) {
        return taskList.stream()
            .filter(task -> task.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public void deleteTask(Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
    }
}
