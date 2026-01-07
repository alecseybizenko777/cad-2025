package ru.bsuedu.cad.sem2.lab5.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @PreAuthorize("hasRole('TASK_CREATOR')")
    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal UserDetails userDetails) {

        User author = userRepository.findByName(userDetails.getUsername()).orElseThrow();

        task.setAuthor(author);
        taskRepository.save(task);
        ;
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("confirmDeleteTask/{id}")
    public String confirmDeleteTask(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        model.addAttribute("task", task);
        return "confirmDeleteTask";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateTask/{id}")
    public String completeTask(@PathVariable Long id, @RequestParam boolean completed) {

        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(completed);
        taskRepository.save(task);
        return "redirect:/";
    }

    // REST-методы
    @GetMapping("/api")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('TASK_CREATOR')") // Доступ для пользователей и
                                                                                        // модераторов
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/api/{id}")
    @PreAuthorize("hasRole('MODERATOR')") // Доступ только для модераторов
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TASK_CREATOR')")
    @PostMapping("/api")
    public ResponseEntity<Task> addTaskFromApi(@RequestBody Task task,
            @AuthenticationPrincipal UserDetails userDetails) {

        User author = userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        task.setAuthor(author); // Принудительно устанавливаем автора
        task.setCompleted(false); // Дефолтный статус

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(201).body(savedTask);
    }
}
