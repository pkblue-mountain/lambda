package com.example.task_service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("dev")
@SpringBootTest
@Transactional
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void save_shouldPersistTask() {
        Task task = new Task();
        task.setTitle("Repository Test Task");
        task.setDescription("Testing repository save");
        task.setStatus("TODO");

        Task savedTask = taskRepository.save(task);

        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Repository Test Task");
        assertThat(savedTask.getDescription()).isEqualTo("Testing repository save");
        assertThat(savedTask.getStatus()).isEqualTo("TODO");
    }

    @Test
    void findById_shouldReturnTask() {
        Task task = new Task();
        task.setTitle("Find Test Task");
        task.setDescription("Testing findById");
        task.setStatus("TODO");

        Task savedTask = taskRepository.save(task);
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Find Test Task");
    }

    @Test
    void findAll_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");
        task1.setStatus("TODO");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");
        task2.setStatus("IN PROGRESS");

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle).contains("Task 1", "Task 2");
    }

    @Test
    void deleteById_shouldRemoveTask() {
        Task task = new Task();
        task.setTitle("Delete Test Task");
        task.setDescription("Testing delete");
        task.setStatus("TODO");

        Task savedTask = taskRepository.save(task);
        Long id = savedTask.getId();

        taskRepository.deleteById(id);

        Optional<Task> deletedTask = taskRepository.findById(id);
        assertThat(deletedTask).isNotPresent();
    }
}