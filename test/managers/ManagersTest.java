package managers;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldReturnInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер задач не должен быть null");
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        Task createdTask = manager.createTask(task);
        assertNotNull(createdTask, "Менеджер должен создавать задачи");
        assertEquals(1, createdTask.getId(), "ID задачи должен быть присвоен");
    }

    @Test
    void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        Task task = new Task("Test", "Description", TaskStatus.NEW);
        task.setId(1);
        historyManager.addToHistory(task);
        assertEquals(1, historyManager.getHistory().size(), "История должна сохранять задачи");
        assertEquals(task, historyManager.getHistory().get(0), "История должна содержать добавленную задачу");
    }
}