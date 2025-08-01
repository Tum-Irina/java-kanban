package server;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest extends HttpTaskServerTest {

    @Test
    void testCreateAndGetSubtask() throws Exception {
        Epic epic = new Epic("Epic for subtask", "Description");
        String epicJson = gson.toJson(epic);
        sendRequest("/epics", "POST", epicJson);
        Subtask subtask = new Subtask("Test subtask", "Description",
                TaskStatus.NEW, 1, testTime, testDuration);
        String subtaskJson = gson.toJson(subtask);
        HttpResponse<String> response = sendRequest("/subtasks", "POST", subtaskJson);
        assertEquals(201, response.statusCode());
        HttpResponse<String> getResponse = sendRequest("/subtasks/2", "GET", "");
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("Test subtask"));
    }

    @Test
    void testGetSubtasksForEpic() throws Exception {
        Epic epic = new Epic("Epic", "Desc");
        sendRequest("/epics", "POST", gson.toJson(epic));
        Subtask subtask1 = new Subtask("Sub 1", "Desc", TaskStatus.NEW, 1,
                testTime, Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Sub 2", "Desc", TaskStatus.NEW, 1,
                testTime.plusHours(1), Duration.ofHours(1));
        sendRequest("/subtasks", "POST", gson.toJson(subtask1));
        sendRequest("/subtasks", "POST", gson.toJson(subtask2));
        HttpResponse<String> response = sendRequest("/epics/1/subtasks", "GET", "");
        assertEquals(200, response.statusCode());
    }
}