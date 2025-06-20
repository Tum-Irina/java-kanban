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

    @Test
    void shouldRemoveDuplicatesAndKeepLastOccurrence() {
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW);
        task1.setId(1);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1);
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Должна остаться только последняя версия");
        assertEquals(task1, history.get(0));
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW);
        task1.setId(1);
        historyManager.addToHistory(task1);
        historyManager.removeFromHistory(1);
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после удаления");
    }

    @Test
    void shouldMaintainInsertionOrder() {
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Desc", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task1);
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Должно быть 2 задачи");
        assertEquals(task2, history.get(0), "Порядок должен сохраняться");
        assertEquals(task1, history.get(1), "Порядок должен сохраняться");
    }
}