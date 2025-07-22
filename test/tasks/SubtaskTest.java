package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 10, 0);

    @Test
    void shouldStoreEpicIdAndTimeParameters() {
        Subtask subtask = new Subtask("Sub", "Desc", TaskStatus.NEW, 1,
                testTime, Duration.ofHours(1));
        assertEquals(1, subtask.getEpicId());
        assertEquals(testTime, subtask.getStartTime());
        assertEquals(Duration.ofHours(1), subtask.getDuration());
        assertEquals(testTime.plusHours(1), subtask.getEndTime());
    }

    @Test
    void shouldUpdateTimeParametersForSubtask() {
        Subtask subtask = new Subtask("Sub", "Desc", TaskStatus.NEW, 1, null, null);
        subtask.setStartTime(testTime);
        subtask.setDuration(Duration.ofMinutes(30));
        assertEquals(testTime, subtask.getStartTime());
        assertEquals(Duration.ofMinutes(30), subtask.getDuration());
        assertEquals(testTime.plusMinutes(30), subtask.getEndTime());
        subtask.setStartTime(testTime.plusHours(1));
        assertEquals(testTime.plusHours(1).plusMinutes(30), subtask.getEndTime());
    }

    @Test
    void subtasksShouldBeEqualIfTheirIdsAreEqual() {
        Subtask subtask1 = new Subtask("Name 1", "Description 1", TaskStatus.NEW, 1,
                testTime, Duration.ofHours(1));
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Name 2", "Description 2", TaskStatus.NEW, 1,
                testTime, Duration.ofHours(1));
        subtask2.setId(1);
        assertEquals(subtask1, subtask2, "Подзадачи равны друг другу, если равен их id");
    }

    @Test
    void subtasksWithDifferentIdsShouldNotBeEqual() {
        Subtask subtask1 = new Subtask("Name 1", "Description 1", TaskStatus.NEW, 1,
                testTime, Duration.ofHours(1));
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Name 1", "Description 1", TaskStatus.NEW, 1,
                testTime, Duration.ofHours(1));
        subtask2.setId(2);
        assertNotEquals(subtask1, subtask2, "Подзадачи с разным id не должны быть равны");
    }
}