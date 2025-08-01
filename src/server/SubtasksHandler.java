package server;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class SubtasksHandler extends BaseHttpHandler<Subtask> {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/subtasks")) {
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
            Subtask subtask = gson.fromJson(readRequestBody(exchange), Subtask.class);
            if (subtask.getEpicId() == 0 || taskManager.getEpicById(subtask.getEpicId()) == null) {
                sendNotFound(exchange);
                return;
            }

            if (subtask.getId() == 0) {
                handleCreate(exchange, subtask);
            } else {
                handleUpdate(exchange, subtask);
            }
        } catch (ManagerSaveException e) {
            sendNotAcceptable(exchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/subtasks")) {
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
    protected List<Subtask> getAllItems() {
        return taskManager.getAllSubtasks();
    }

    @Override
    protected Subtask getItemById(int id) throws NoSuchElementException {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask == null) throw new NoSuchElementException();
        return subtask;
    }

    @Override
    protected void createItem(Subtask item) throws ManagerSaveException {
        taskManager.createSubtask(item);
    }

    @Override
    protected void updateItem(Subtask item) throws ManagerSaveException {
        taskManager.updateSubtask(item);
    }

    @Override
    protected void deleteAllItems() {
        taskManager.deleteAllSubtasks();
    }

    @Override
    protected void deleteItem(int id) throws NoSuchElementException {
        taskManager.deleteSubtask(id);
    }
}