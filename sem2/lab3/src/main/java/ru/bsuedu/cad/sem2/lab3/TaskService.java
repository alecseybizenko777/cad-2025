package ru.bsuedu.cad.sem2.lab3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public Task getTask(Long id) {
        return taskRepository.getTask(id);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    public void completeTask(Long id) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            task.setCompleted(true);
        }
    }
}
