package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 10, 0);

    protected abstract T createManager();

    @BeforeEach
    abstract void setUp() throws IOException; {
        manager = createManager();
    }

    @Test
    void shouldCreateTaskWithTimeParameters() {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(1)));
        assertNotNull(task.getStartTime());
        assertEquals(testTime, task.getStartTime());
        assertEquals(Duration.ofHours(1), task.getDuration());
        assertEquals(testTime.plusHours(1), task.getEndTime());
    }

    @Test
    void shouldUpdateTaskTimeParameters() {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(1)));
        Task updated = new Task("Updated", "Desc", TaskStatus.IN_PROGRESS, testTime.plusHours(2), Duration.ofMinutes(30));
        updated.setId(task.getId());
        manager.updateTask(updated);
        Task saved = manager.getTaskById(task.getId());
        assertEquals(testTime.plusHours(2), saved.getStartTime());
        assertEquals(Duration.ofMinutes(30), saved.getDuration());
    }

    @Test
    void shouldDeleteTask() {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(1)));
        manager.deleteTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    void shouldCreateAndFindEpic() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        Epic found = manager.getEpicById(epic.getId());
        assertNotNull(found);
        assertEquals(epic, found);
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        Epic updated = new Epic("Updated", "New desc");
        updated.setId(epic.getId());
        manager.updateEpic(updated);
        Epic saved = manager.getEpicById(epic.getId());
        assertEquals("Updated", saved.getName());
    }

    @Test
    void shouldCreateAndFindSubtask() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        Subtask subtask = manager.createSubtask(new Subtask("Sub", "Desc", TaskStatus.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofHours(1)));
        assertNotNull(manager.getSubtaskById(subtask.getId()));
        assertEquals(epic.getId(), subtask.getEpicId());
    }

    @Test
    void epicStatusShouldBeNewWhenNoSubtasks() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void epicStatusShouldBeNewWhenAllSubtasksNew() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.createSubtask(new Subtask("Sub1", "Desc", TaskStatus.NEW, epic.getId(),
                testTime, Duration.ofHours(1)));
        manager.createSubtask(new Subtask("Sub2", "Desc", TaskStatus.NEW, epic.getId(),
                LocalDateTime.now(), Duration.ofHours(1)));
        assertEquals(TaskStatus.NEW, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void epicStatusShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.createSubtask(new Subtask("Sub1", "Desc", TaskStatus.DONE, epic.getId(),
                testTime, Duration.ofHours(1)));
        manager.createSubtask(new Subtask("Sub2", "Desc", TaskStatus.DONE, epic.getId(),
                LocalDateTime.now(), Duration.ofHours(1)));
        assertEquals(TaskStatus.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void epicStatusShouldBeInProgressWhenMixedStatuses() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.createSubtask(new Subtask("Sub1", "Desc", TaskStatus.NEW, epic.getId(),
                testTime, Duration.ofHours(1)));
        manager.createSubtask(new Subtask("Sub2", "Desc", TaskStatus.DONE, epic.getId(),
                LocalDateTime.now(), Duration.ofHours(1)));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldCalculateEpicTimeCorrectly() {
        LocalDateTime start = LocalDateTime.now();
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.createSubtask(new Subtask("Sub1", "Desc", TaskStatus.NEW, epic.getId(),
                start, Duration.ofHours(1)));
        manager.createSubtask(new Subtask("Sub2", "Desc", TaskStatus.NEW, epic.getId(),
                start.plusHours(2), Duration.ofHours(2)));
        Epic saved = manager.getEpicById(epic.getId());
        assertEquals(start, saved.getStartTime());
        assertEquals(Duration.ofHours(3), saved.getDuration());
    }

    @Test
    void shouldAddToHistoryWhenTaskViewed() {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(1)));
        manager.getTaskById(task.getId());
        assertEquals(1, manager.getHistory().size());
        assertEquals(task, manager.getHistory().get(0));
    }

}