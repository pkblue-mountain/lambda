package com.example.task_service.lambda;

import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.task_service.Task;
import com.example.task_service.TaskService;

import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@Component
public class TaskHttpHandler {

    private final TaskService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public TaskHttpHandler(TaskService service) {
        this.service = service;
    }

    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent req) {
        try {
            String method = req.getHttpMethod();
            String path = req.getPath();

            if ("GET".equals(method) && "/tasks".equals(path)) {
                return handleGetTasks();
            } else if ("POST".equals(method) && "/tasks".equals(path)) {
                return handleCreateTask(req);
            } else if (path.startsWith("/tasks/")) {
                String idStr = path.substring("/tasks/".length());
                Long id = Long.parseLong(idStr);
                if ("PUT".equals(method)) {
                    return handleUpdateTask(req, id);
                } else if ("PATCH".equals(method)) {
                    return handleUpdateTaskStatus(req, id);
                } else if ("DELETE".equals(method)) {
                    return handleDeleteTask(id);
                } else {
                    return errorResponse(404, "Not Found");
                }
            } else {
                return errorResponse(404, "Not Found");
            }
        } catch (Exception e) {
            return errorResponse(500, "Internal Server Error");
        }
    }

    private APIGatewayProxyResponseEvent handleGetTasks() throws Exception {
        List<Task> tasks = service.getAllTasks();
        String body = mapper.writeValueAsString(tasks);
        return successResponse(body);
    }

    private APIGatewayProxyResponseEvent handleCreateTask(APIGatewayProxyRequestEvent req) throws Exception {
        Task task = mapper.readValue(req.getBody(), Task.class);
        Task created = service.createTask(task);
        String body = mapper.writeValueAsString(created);
        return successResponse(body);
    }

    private APIGatewayProxyResponseEvent handleUpdateTask(APIGatewayProxyRequestEvent req, Long id) throws Exception {
        Task updatedTask = mapper.readValue(req.getBody(), Task.class);
        Task result = service.updateTask(id, updatedTask);
        String body = mapper.writeValueAsString(result);
        return successResponse(body);
    }

    private APIGatewayProxyResponseEvent handleUpdateTaskStatus(APIGatewayProxyRequestEvent req, Long id) throws Exception {
        Map<String, String> map = mapper.readValue(req.getBody(), Map.class);
        String status = map.get("status");
        Task result = service.updateTaskStatus(id, status);
        String body = mapper.writeValueAsString(result);
        return successResponse(body);
    }

    private APIGatewayProxyResponseEvent handleDeleteTask(Long id) throws Exception {
        service.deleteTask(id);
        return successResponseWithoutBody();
    }

    private APIGatewayProxyResponseEvent successResponse(String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(body)
                .withHeaders(Map.of("Content-Type", "application/json"));
    }

    private APIGatewayProxyResponseEvent successResponseWithoutBody() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(Map.of("Content-Type", "application/json"));
    }

    private APIGatewayProxyResponseEvent errorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody("{\"error\":\"" + message + "\"}")
                .withHeaders(Map.of("Content-Type", "application/json"));
    }
}
