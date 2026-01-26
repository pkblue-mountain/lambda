package com.example.task_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("TODO");

        when(taskService.createTask(task)).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("TODO");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("IN PROGRESS");

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].status").value("IN PROGRESS"));

        verify(taskService).getAllTasks();
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus("COMPLETED");

        when(taskService.updateTask(1L, updatedTask)).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(taskService).updateTask(1L, updatedTask);
    }

    @Test
    void updateTaskStatus_shouldUpdateOnlyStatus() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setStatus("COMPLETED");

        when(taskService.updateTaskStatus(1L, "COMPLETED")).thenReturn(updatedTask);

        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("status", "COMPLETED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(taskService).updateTaskStatus(1L, "COMPLETED");
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService).deleteTask(1L);
    }
}
