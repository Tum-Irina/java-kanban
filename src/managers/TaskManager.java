package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    ArrayList<Subtask> getSubtasksByEpicId(int epicId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtask(int id);

    ArrayList<Task> getHistory();

    List<Task> getPrioritizedTasks();
}