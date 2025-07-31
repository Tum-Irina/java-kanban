package server;

import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest extends HttpTaskServerTest {

    @Test
    void testCreateEpic() throws Exception {
        Epic epic = new Epic("Test Epic", "Description");
        HttpResponse<String> response = sendRequest("/epics", "POST", gson.toJson(epic));
        assertEquals(201, response.statusCode());
        assertTrue(sendRequest("/epics/1", "GET", "").body().contains("Test Epic"));
    }

    @Test
    void testDeleteEpic() throws Exception {
        Epic epic = new Epic("To delete", "Desc");
        sendRequest("/epics", "POST", gson.toJson(epic));
        HttpResponse<String> deleteResponse = sendRequest("/epics/1", "DELETE", "");
        assertEquals(200, deleteResponse.statusCode());
        HttpResponse<String> getResponse = sendRequest("/epics/1", "GET", "");
        assertEquals(404, getResponse.statusCode());
    }
}