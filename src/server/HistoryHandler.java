package server;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class HistoryHandler extends BaseHttpHandler<Task> {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        handleGetCollection(exchange);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        sendNotAcceptable(exchange);
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        sendNotAcceptable(exchange);
    }

    @Override
    protected List<Task> getAllItems() {
        return taskManager.getHistory();
    }

    @Override
    protected Task getItemById(int id) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void createItem(Task item) throws ManagerSaveException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void updateItem(Task item) throws ManagerSaveException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void deleteAllItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void deleteItem(int id) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }
}