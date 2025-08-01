package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusTests extends HttpTaskServerTest {

    @BeforeEach
    public void setUpTestData() throws IOException, InterruptedException {
        Task testTask = new Task("Test Task", "Desc", TaskStatus.NEW, testTime, testDuration);
        Epic testEpic = new Epic("Test Epic", "Desc");
        Subtask testSubtask = new Subtask("Test Subtask", "Desc", TaskStatus.NEW, 1,
                testTime.plusHours(1), Duration.ofMinutes(45));
        sendRequest("/tasks", "POST", gson.toJson(testTask));
        sendRequest("/epics", "POST", gson.toJson(testEpic));
        sendRequest("/subtasks", "POST", gson.toJson(testSubtask));
    }

    @Test
    void testSuccessStatuses() throws Exception {
        assertEquals(200, sendRequest("/tasks", "GET", "").statusCode());
        assertEquals(200, sendRequest("/tasks/1", "GET", "").statusCode());
        assertEquals(200, sendRequest("/epics", "GET", "").statusCode());
        assertEquals(200, sendRequest("/history", "GET", "").statusCode());
        assertEquals(200, sendRequest("/tasks/1", "DELETE", "").statusCode());
    }

    @Test
    void testNotFoundStatuses() throws Exception {
        assertEquals(404, sendRequest("/tasks/999", "GET", "").statusCode());
        assertEquals(404, sendRequest("/subtasks/999", "GET", "").statusCode());
        assertEquals(404, sendRequest("/epics/999", "GET", "").statusCode());
    }

    @Test
    void testNotAllowedStatuses() throws Exception {
        assertEquals(406, sendRequest("/history", "POST", "{}").statusCode());
        assertEquals(406, sendRequest("/prioritized", "DELETE", "").statusCode());
    }

    @Test
    void testBadRequestStatuses() throws Exception {
        assertEquals(500, sendRequest("/tasks", "POST", "{invalid}").statusCode());
        Subtask invalidSubtask = new Subtask("Invalid", "Desc", TaskStatus.NEW, 999,
                testTime, testDuration);
        assertEquals(404, sendRequest("/subtasks", "POST", gson.toJson(invalidSubtask)).statusCode());
    }
}