package com.example.task_service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

 
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @PatchMapping("/{id}")
    public Task updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> map) {
        return taskService.updateTaskStatus(id, map.get("status"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
