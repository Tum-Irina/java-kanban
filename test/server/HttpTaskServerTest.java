package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    protected TaskManager manager;
    protected HttpTaskServer taskServer;
    protected HttpClient client;
    protected Gson gson;
    protected final String baseUrl = "http://localhost:8080";
    protected final LocalDateTime testTime = LocalDateTime.now();
    protected final Duration testDuration = Duration.ofMinutes(30);

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        client = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    protected HttpResponse<String> sendRequest(String endpoint, String method, String body)
            throws IOException, InterruptedException {
        URI url = URI.create(baseUrl + endpoint);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(url);

        switch (method) {
            case "GET" -> requestBuilder.GET();
            case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
            case "DELETE" -> requestBuilder.DELETE();
        }
        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
}