package com.example.task_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void createTask_shouldSaveAndReturnTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setStatus("IN PROGRESS");

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.createTask(task);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("This is a test task", savedTask.getDescription());
        assertEquals("IN PROGRESS", savedTask.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    public void getAllTasks_shouldReturnTaskList() {
        Task task1 = new Task();
        Task task2 = new Task();

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    public void updateTask_shouldUpdateAllFields() {
        Task existingTask = new Task();
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Desc");
        existingTask.setStatus("TODO");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Desc");
        updatedTask.setStatus("COMPLETED");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals("COMPLETED", result.getStatus());
        verify(taskRepository).findById(1L);
    }

    @Test
    public void updateTask_shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskService.updateTask(1L, new Task()));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void updateTaskStatus_shouldUpdateOnlyStatus() {
        Task existingTask = new Task();
        existingTask.setStatus("TODO");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        Task result = taskService.updateTaskStatus(1L, "IN PROGRESS");

        assertEquals("IN PROGRESS", result.getStatus());
        verify(taskRepository).findById(1L);
    }

    @Test
    public void deleteTask_shouldCallRepositoryDelete() {
        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }
}
