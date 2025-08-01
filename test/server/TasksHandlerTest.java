package server;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TasksHandlerTest extends HttpTaskServerTest {

    @Test
    void testCreateAndGetTask() throws Exception {
        Task task = new Task("Test", "Desc", TaskStatus.NEW, testTime, testDuration);
        HttpResponse<String> createResponse = sendRequest("/tasks", "POST", gson.toJson(task));
        assertEquals(201, createResponse.statusCode());
        HttpResponse<String> getResponse = sendRequest("/tasks/1", "GET", "");
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("Test"));
    }
}