package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

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