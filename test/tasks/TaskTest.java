package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 10, 0);

    @Test
    void shouldCalculateEndTimeCorrectly() {
        Task task = new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(2));
        assertEquals(testTime.plusHours(2), task.getEndTime());
    }

    @Test
    void shouldHandleNullTimeParameters() {
        Task task = new Task("Task", "Desc", TaskStatus.NEW, null, null);
        assertNull(task.getStartTime());
        assertNull(task.getDuration());
        assertNull(task.getEndTime());
    }

    @Test
    void shouldUpdateTimeParametersForTask() {
        Task task = new Task("Task", "Desc", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task.setStartTime(testTime.plusDays(1));
        task.setDuration(Duration.ofMinutes(30));
        task.updateEndTime();
        assertEquals(testTime.plusDays(1), task.getStartTime());
        assertEquals(Duration.ofMinutes(30), task.getDuration());
        assertEquals(testTime.plusDays(1).plusMinutes(30), task.getEndTime());
        task.setStartTime(null);
        task.setDuration(null);
        task.updateEndTime();
        assertNull(task.getEndTime());
    }

    @Test
    void tasksShouldBeEqualIfTheirIdsAreEqual() {
        Task task1 = new Task("Name 1", "Description 1", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task1.setId(1);
        Task task2 = new Task("Name 2", "Description 2", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task2.setId(1);
        assertEquals(task1, task2, "Задачи равны друг другу, если равен их id");
    }

    @Test
    void tasksWithDifferentIdsShouldNotBeEqual() {
        Task task1 = new Task("Name 1", "Description 1", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task1.setId(1);
        Task task2 = new Task("Name 1", "Description 1", TaskStatus.NEW, testTime, Duration.ofHours(1));
        task2.setId(2);
        assertNotEquals(task1, task2, "Задачи с разным id не должны быть равны");
    }
}