package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtasksShouldBeEqualIfTheirIdsAreEqual() {
        Subtask subtask1 = new Subtask("Name 1", "Description 1", TaskStatus.NEW, 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Name 2", "Description 2", TaskStatus.NEW, 1);
        subtask2.setId(1);
        assertEquals(subtask1, subtask2, "Подзадачи равны друг другу, если равен их id");
    }

    @Test
    void subtasksWithDifferentIdsShouldNotBeEqual() {
        Subtask subtask1 = new Subtask("Name 1", "Description 1", TaskStatus.NEW,1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Name 1", "Description 1", TaskStatus.NEW, 1);
        subtask2.setId(2);
        assertNotEquals(subtask1, subtask2, "Подзадачи с разным id не должны быть равны");
    }
}