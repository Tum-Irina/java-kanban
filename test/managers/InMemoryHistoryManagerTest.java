package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static final int MAX_HISTORY_SIZE = 10;
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
    void historyShouldNotExceedMaxSize() {
        final int extraTasks = 3;
        final int totalTasks = MAX_HISTORY_SIZE + extraTasks;
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= totalTasks; i++) {
            tasks.add(new Task("Task " + i, "Description " + i, TaskStatus.NEW));
        }
        for (Task task : tasks) {
            historyManager.addToHistory(task);
        }
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(MAX_HISTORY_SIZE, history.size(), "Размер истории не должен превышать MAX_HISTORY_SIZE");
        for (int i = 0; i < MAX_HISTORY_SIZE; i++) {
            Task expected = tasks.get(extraTasks + i);
            Task actual = history.get(i);
            assertEquals(expected, actual, "В истории должны оставаться последние " + MAX_HISTORY_SIZE + " задач");
        }
    }
}