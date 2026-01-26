package com.example.task_service;

import java.util.List;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());

        return task;
    }

    @Transactional
    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);

        return task;
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}