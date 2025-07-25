package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;
    private final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 10, 0);

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Override
    protected FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loaded.getAllTasks().isEmpty());
        assertTrue(loaded.getAllEpics().isEmpty());
        assertTrue(loaded.getAllSubtasks().isEmpty());
        assertTrue(loaded.getHistory().isEmpty());
    }

    @Test
    void shouldSaveAndLoadTasks() {
        Task task = manager.createTask(new Task("Task 1", "Description", TaskStatus.NEW,
                testTime, Duration.ofHours(1)));
        Epic epic = manager.createEpic(new Epic("Epic 1", "Description"));
        Subtask subtask = manager.createSubtask(new Subtask("Subtask 1", "Description",
                TaskStatus.NEW, epic.getId(), testTime.plusHours(1), Duration.ofHours(1)));
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(1, loaded.getAllTasks().size());
        assertEquals(task, loaded.getTaskById(task.getId()));
        assertEquals(1, loaded.getAllEpics().size());
        assertEquals(epic, loaded.getEpicById(epic.getId()));
        assertEquals(1, loaded.getAllSubtasks().size());
        assertEquals(subtask, loaded.getSubtaskById(subtask.getId()));
        assertEquals(epic.getId(), loaded.getSubtaskById(subtask.getId()).getEpicId());
        assertEquals(1, loaded.getEpicById(epic.getId()).getSubtaskIds().size());
    }

    @Test
    void shouldSaveAndLoadHistory() {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW,
                testTime, Duration.ofHours(1)));
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.updateTask(task);
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(2, loaded.getHistory().size());
        assertEquals(task, loaded.getHistory().get(0));
        assertEquals(epic, loaded.getHistory().get(1));
    }

}