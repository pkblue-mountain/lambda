package com.example.task_service.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.task_service.TaskServiceApplication;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ConfigurableApplicationContext applicationContext = SpringApplication
            .run(TaskServiceApplication.class);

    private final TaskHttpHandler httpHandler = applicationContext.getBean(TaskHttpHandler.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
            Context context) {

        return httpHandler.handle(request);
    }

}
