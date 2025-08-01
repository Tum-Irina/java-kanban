package server;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class TasksHandler extends BaseHttpHandler<Task> {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/tasks")) {
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
            Task task = gson.fromJson(readRequestBody(exchange), Task.class);
            if (task.getId() == 0) {
                handleCreate(exchange, task);
            } else {
                handleUpdate(exchange, task);
            }
        } catch (ManagerSaveException e) {
            sendNotAcceptable(exchange);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/tasks")) {
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
    protected List<Task> getAllItems() {
        return taskManager.getAllTasks();
    }

    @Override
    protected Task getItemById(int id) throws NoSuchElementException {
        Task task = taskManager.getTaskById(id);
        if (task == null) throw new NoSuchElementException();
        return task;
    }

    @Override
    protected void createItem(Task item) throws ManagerSaveException {
        taskManager.createTask(item);
    }

    @Override
    protected void updateItem(Task item) throws ManagerSaveException {
        taskManager.updateTask(item);
    }

    @Override
    protected void deleteAllItems() {
        taskManager.deleteAllTasks();
    }

    @Override
    protected void deleteItem(int id) throws NoSuchElementException {
        taskManager.deleteTaskById(id);
    }
}