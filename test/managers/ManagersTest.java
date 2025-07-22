package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @TempDir
    Path tempDir;
    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    void shouldCreateTaskWithTimeViaDefaultManager() {
        TaskManager manager = Managers.getDefault();
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW,
                testTime, Duration.ofHours(1)));
        assertNotNull(task.getStartTime());
        assertEquals(testTime, manager.getTaskById(task.getId()).getStartTime());
    }

    @Test
    void shouldCreateFileBackedManagerWithTimeSupport() throws Exception {
        File file = tempDir.resolve("tasks.csv").toFile();
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW,
                testTime, Duration.ofHours(1)));
        assertEquals(testTime, manager.getTaskById(task.getId()).getStartTime());
    }

    @Test
    void shouldReturnInitializedTaskManager() throws Exception {
        File testFile = tempDir.resolve("test_tasks.csv").toFile();
        TaskManager manager = new FileBackedTaskManager(testFile);
        Task task = new Task("Task", "Description", TaskStatus.NEW, testTime, Duration.ofHours(1));
        Task createdTask = manager.createTask(task);
        assertNotNull(createdTask, "Менеджер должен создавать задачи");
        assertEquals(1, createdTask.getId(), "ID задачи должен быть присвоен");
    }

    @Test
    void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        Task task = new Task("Test", "Description", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task.setId(1);
        historyManager.addToHistory(task);
        assertEquals(1, historyManager.getHistory().size(), "История должна сохранять задачи");
        assertEquals(task, historyManager.getHistory().get(0), "История должна содержать добавленную задачу");
    }
}