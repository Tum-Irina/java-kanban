package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksShouldBeEqualIfTheirIdsAreEqual() {
        Task task1 = new Task("Name 1", "Description 1", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Name 2", "Description 2", TaskStatus.NEW);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи равны друг другу, если равен их id");
    }

    @Test
    void tasksWithDifferentIdsShouldNotBeEqual() {
        Task task1 = new Task("Name 1", "Description 1", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Name 1", "Description 1", TaskStatus.NEW);
        task2.setId(2);
        assertNotEquals(task1, task2, "Задачи с разным id не должны быть равны");
    }
}