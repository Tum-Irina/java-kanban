package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 10, 0);

    @Test
    void shouldHandleTimeParametersWithoutSubtasks() {
        Epic epic = new Epic("Epic", "Desc");
        assertNull(epic.getStartTime());
        assertNull(epic.getDuration());
        assertNull(epic.getEndTime());
    }

    @Test
    void shouldReturnCalculatedTimeForEpic() {
        Epic epic = new Epic("Epic", "Desc");
        assertNull(epic.getStartTime());
        assertNull(epic.getDuration());
        assertNull(epic.getEndTime());
        assertThrows(UnsupportedOperationException.class, () -> epic.setStartTime(testTime));
        assertThrows(UnsupportedOperationException.class, () -> epic.setDuration(Duration.ofHours(1)));
    }

    @Test
    void epicsShouldBeEqualIfTheirIdsAreEqual() {
        Epic epic1 = new Epic("Name 1", "Description 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Name 2", "Description 2");
        epic2.setId(1);
        assertEquals(epic1, epic2, "Эпики равны друг другу, если равен их id");
    }

    @Test
    void epicsWithDifferentIdsShouldNotBeEqual() {
        Epic epic1 = new Epic("Name 1", "Description 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Name 1", "Description 1");
        epic2.setId(2);
        assertNotEquals(epic1, epic2, "Эпики с разным id не должны быть равны");
    }
}