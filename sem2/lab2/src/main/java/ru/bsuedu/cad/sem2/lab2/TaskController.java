package ru.bsuedu.cad.sem2.lab2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        taskService.addTask(task);;
        return "redirect:/";
    }

    @GetMapping("confirmDeleteTask/{id}")
    public String confirmDeleteTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("task", task);
        return "confirmDeleteTask";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/completeTask/{id}")
    public String completeTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.completeTask(id);
        return "redirect:/";
    }
}
