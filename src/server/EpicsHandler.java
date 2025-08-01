package server;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class EpicsHandler extends BaseHttpHandler<Epic> {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/epics")) {
            handleGetCollection(exchange);
        } else {
            try {
                int id = extractIdFromPath(path);
                handleGetSingle(exchange, id);
            } catch (IllegalArgumentException | NoSuchElementException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        try {
            Epic epic = gson.fromJson(readRequestBody(exchange), Epic.class);
            if (epic.getId() == 0) {
                handleCreate(exchange, epic);
            } else {
                handleUpdate(exchange, epic);
            }
        } catch (ManagerSaveException e) {
            sendInternalError(exchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/epics")) {
            handleDeleteAll(exchange);
        } else {
            try {
                int id = extractIdFromPath(path);
                handleDeleteSingle(exchange, id);
            } catch (IllegalArgumentException | NoSuchElementException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected List<Epic> getAllItems() {
        return taskManager.getAllEpics();
    }

    @Override
    protected Epic getItemById(int id) throws NoSuchElementException {
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) throw new NoSuchElementException();
        return epic;
    }

    @Override
    protected void createItem(Epic item) throws ManagerSaveException {
        taskManager.createEpic(item);
    }

    @Override
    protected void updateItem(Epic item) throws ManagerSaveException {
        taskManager.updateEpic(item);
    }

    @Override
    protected void deleteAllItems() {
        taskManager.deleteAllEpics();
    }

    @Override
    protected void deleteItem(int id) throws NoSuchElementException {
        taskManager.deleteEpicById(id);
    }
}