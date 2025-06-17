package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Task", "Description", TaskStatus.NEW);
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.addToHistory(task);
        ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История задач не должна быть null");
        assertEquals(1, history.size(), "В истории должна быть ровно 1 задача");
        assertEquals(task, history.get(0), "Задача в истории должна соответствовать добавленной");
    }

}