package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split(System.lineSeparator());
            boolean isHistorySection = false;

            for (String line : lines) {
                if (line.isBlank()) {
                    isHistorySection = true;
                    continue;
                }
                if (line.equals(CSVFormat.getHeader())) {
                    continue;
                }
                if (isHistorySection) {
                    restoreHistory(manager, line);
                } else {
                    restoreTask(manager, line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
        return manager;
    }

    private static void restoreHistory(FileBackedTaskManager manager, String line) {
        String[] historyIds = line.split(",");
        for (String idStr : historyIds) {
            int id = Integer.parseInt(idStr);
            Task task = manager.findTaskById(id);
            if (task != null) {
                manager.historyManager.addToHistory(task);
            }
        }
    }

    private Task findTaskById(int id) {
        if (tasks.containsKey(id)) return tasks.get(id);
        if (epics.containsKey(id)) return epics.get(id);
        if (subtasks.containsKey(id)) return subtasks.get(id);
        return null;
    }

    private static void restoreTask(FileBackedTaskManager manager, String line) {
        Task task = CSVFormat.fromString(line);
        if (task == null) return;

        if (task.getId() >= manager.nextId) {
            manager.nextId = task.getId() + 1;
        }

        TaskType type = CSVFormat.getTaskType(task);
        switch (type) {
            case TASK:
                manager.tasks.put(task.getId(), task);
                break;
            case EPIC:
                manager.epics.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                int epicId = subtask.getEpicId();
                manager.subtasks.put(subtask.getId(), subtask);
                manager.epics.get(epicId).addSubtaskId(subtask.getId());
                manager.updateEpicStatus(epicId);
                break;
        }
    }

    @Override
    public Task createTask(Task task) {
        Task createTask = super.createTask(task);
        save();
        return createTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createEpic = super.createEpic(epic);
        save();
        return createEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createSubtask = super.createSubtask(subtask);
        save();
        return createSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormat.getHeader());
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(CSVFormat.toString(task));
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(CSVFormat.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(CSVFormat.toString(subtask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(CSVFormat.toString(historyManager.getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }
}