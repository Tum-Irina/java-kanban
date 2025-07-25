package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void shouldDetectTimeOverlapsForTasks() {
        LocalDateTime time = LocalDateTime.now();
        manager.createTask(new Task("Task1", "Desc", TaskStatus.NEW,
                time, Duration.ofHours(1)));
        Task overlappingTask = new Task("Task2", "Desc", TaskStatus.NEW,
                time.plusMinutes(30), Duration.ofHours(1));
        assertThrows(ManagerSaveException.class, () -> manager.createTask(overlappingTask));
    }

    @Test
    void shouldDetectTimeOverlapsForSubtasks() {
        LocalDateTime time = LocalDateTime.now();
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        manager.createSubtask(new Subtask("Sub1", "Desc", TaskStatus.NEW,
                epic.getId(), time, Duration.ofHours(1)));
        Subtask overlappingSub = new Subtask("Sub2", "Desc", TaskStatus.NEW,
                epic.getId(), time.plusMinutes(30), Duration.ofHours(1));

        assertThrows(ManagerSaveException.class, () -> manager.createSubtask(overlappingSub));
    }

    @Test
    void shouldCalculateEpicTimeThroughManager() {
        Epic epic = manager.createEpic(new Epic("Epic", "Desc"));
        Subtask sub1 = manager.createSubtask(new Subtask("Sub1", "Desc",
                TaskStatus.NEW, epic.getId(), testTime, Duration.ofHours(1)));
        Subtask sub2 = manager.createSubtask(new Subtask("Sub2", "Desc",
                TaskStatus.NEW, epic.getId(), testTime.plusHours(2), Duration.ofHours(2)));

        // Проверяем расчетные значения
        assertEquals(testTime, manager.getEpicById(epic.getId()).getStartTime());
        assertEquals(Duration.ofHours(3), manager.getEpicById(epic.getId()).getDuration());
        assertEquals(testTime.plusHours(4), manager.getEpicById(epic.getId()).getEndTime());
    }

    @Test
    void shouldAddAndFindTask() {
        Task task = new Task("Task", "Description", TaskStatus.NEW, testTime, Duration.ofHours(1));
        Task createdTask = manager.createTask(task);
        Task foundTask = manager.getTaskById(createdTask.getId());
        ArrayList<Task> tasks = manager.getAllTasks();

        assertNotNull(createdTask, "Задача не создана");
        assertEquals(task.getName(), createdTask.getName(), "Названия задач не совпадают");
        assertEquals(createdTask, foundTask, "Задача не найдена по id");
        assertEquals(createdTask.getDescription(), foundTask.getDescription(), "Описания задач не совпадают");
        assertEquals(createdTask.getStatus(), foundTask.getStatus(), "Статусы задач не совпадают");
        assertNotNull(tasks, "Список задач не должен быть null");
        assertEquals(1, tasks.size(), "В списке должна быть ровно 1 задача");
        assertEquals(task.getName(), tasks.get(0).getName(), "Задача в списке должна соответствовать добавленной");
    }

    @Test
    void shouldAddAndFindEpic() {
        Epic epic = new Epic("Epic", "Description");
        Epic createdEpic = manager.createEpic(epic);
        Epic foundEpic = manager.getEpicById(createdEpic.getId());
        ArrayList<Epic> epics = manager.getAllEpics();

        assertNotNull(createdEpic, "Эпик не создан");
        assertEquals(epic.getName(), createdEpic.getName(), "Названия эпиков не совпадают");
        assertEquals(createdEpic, foundEpic, "Эпик не найден по id");
        assertEquals(createdEpic.getDescription(), foundEpic.getDescription(), "Описания эпиков не совпадают");
        assertEquals(createdEpic.getStatus(), foundEpic.getStatus(), "Статусы эпиков не совпадают");
        assertNotNull(epics, "Список эпиков не должен быть null");
        assertEquals(1, epics.size(), "В списке должен быть ровно 1 эпик");
        assertEquals(epic.getName(), epics.get(0).getName(), "Эпик в списке должен соответствовать добавленному");
    }

    @Test
    void shouldAddAndFindSubtask() {
        Epic epic = new Epic("Epic", "Description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", TaskStatus.NEW, epic.getId(),
                testTime, Duration.ofHours(1));
        Subtask createdSubtask = manager.createSubtask(subtask);
        Subtask foundSubtask = manager.getSubtaskById(createdSubtask.getId());
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(createdSubtask, "Подзадача не создана");
        assertEquals(subtask.getName(), createdSubtask.getName(), "Названия подзадач не совпадают");
        assertEquals(createdSubtask, foundSubtask, "Подзадача не найдена по id");
        assertEquals(createdSubtask.getDescription(), foundSubtask.getDescription(), "Описания подзадач не совпадают");
        assertEquals(createdSubtask.getStatus(), foundSubtask.getStatus(), "Статусы подзадач не совпадают");
        assertNotNull(subtasks, "Список подзадач не должен быть null");
        assertEquals(1, subtasks.size(), "В списке должна быть ровно 1 подзадача");
        assertEquals(subtask.getName(), subtasks.get(0).getName(), "Подзадача в списке должна соответствовать добавленной");
    }

    @Test
    void shouldNotFindNonExistentTask() {
        assertNull(manager.getTaskById(999), "Найдена несуществующая задача");
    }

    @Test
    void shouldNotFindNonExistentEpic() {
        assertNull(manager.getEpicById(999), "Найден несуществующий эпик");
    }

    @Test
    void shouldNotFindNonExistentSubtask() {
        assertNull(manager.getSubtaskById(999), "Найдена несуществующая подзадача");
    }
}

