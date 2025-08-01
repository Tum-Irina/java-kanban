package server;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends HttpTaskServerTest {

    @Test
    void testGetHistory() throws Exception {
        Task task = new Task("Task for history", "Desc",
                TaskStatus.NEW, testTime, testDuration);
        sendRequest("/tasks", "POST", gson.toJson(task));
        sendRequest("/tasks/1", "GET", "");
        HttpResponse<String> response = sendRequest("/history", "GET", "");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task for history"));
    }

    @Test
    void testEmptyHistory() throws Exception {
        HttpResponse<String> response = sendRequest("/history", "GET", "");
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }
}