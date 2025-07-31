package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.ManagerSaveException;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class BaseHttpHandler<T extends Task> implements HttpHandler {
    protected static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (NoSuchElementException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendNotAcceptable(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    protected abstract void handleGet(HttpExchange exchange) throws IOException;

    protected abstract void handlePost(HttpExchange exchange) throws IOException;

    protected abstract void handleDelete(HttpExchange exchange) throws IOException;

    protected abstract List<T> getAllItems();

    protected abstract T getItemById(int id) throws NoSuchElementException;

    protected abstract void createItem(T item) throws ManagerSaveException;

    protected abstract void updateItem(T item) throws ManagerSaveException;

    protected abstract void deleteAllItems();

    protected abstract void deleteItem(int id) throws NoSuchElementException;

    protected void handleGetCollection(HttpExchange exchange) throws IOException {
        sendJson(exchange, getAllItems());
    }

    protected void handleGetSingle(HttpExchange exchange, int id) throws IOException, NoSuchElementException {
        sendJson(exchange, getItemById(id));
    }

    protected void handleCreate(HttpExchange exchange, T item) throws IOException, ManagerSaveException {
        createItem(item);
        sendCreated(exchange);
    }

    protected void handleUpdate(HttpExchange exchange, T item) throws IOException, ManagerSaveException {
        updateItem(item);
        sendSuccess(exchange);
    }

    protected void handleDeleteAll(HttpExchange exchange) throws IOException {
        deleteAllItems();
        sendSuccess(exchange);
    }

    protected void handleDeleteSingle(HttpExchange exchange, int id) throws IOException, NoSuchElementException {
        deleteItem(id);
        sendSuccess(exchange);
    }

    protected void sendJson(HttpExchange exchange, Object data) throws IOException {
        String response = gson.toJson(data);
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", CONTENT_TYPE_JSON);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    protected void sendSuccess(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        exchange.close();
    }

    protected void sendCreated(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, -1);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
        exchange.close();
    }

    protected void sendNotAcceptable(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, -1);
        exchange.close();
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
    }

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected int extractIdFromPath(String path) throws IllegalArgumentException {
        String[] parts = path.split("/");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid path format");
        }
        return Integer.parseInt(parts[2]);
    }
}