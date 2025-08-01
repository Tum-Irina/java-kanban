package server;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest extends HttpTaskServerTest {

    @Test
    void testGetPrioritizedTasks() throws Exception {
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW,
                testTime.plusHours(1), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Desc", TaskStatus.NEW,
                testTime, Duration.ofHours(1));
        sendRequest("/tasks", "POST", gson.toJson(task1));
        sendRequest("/tasks", "POST", gson.toJson(task2));
        HttpResponse<String> response = sendRequest("/prioritized", "GET", "");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().indexOf("Task 2") < response.body().indexOf("Task 1"));
    }
}