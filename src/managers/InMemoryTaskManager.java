package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int nextId = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
    );

    private int getNextId() {
        return nextId++;
    }

    @Override
    public Task createTask(Task task) {
        if (hasTimeOverlap(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (hasTimeOverlap(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }
        int epicId = subtask.getEpicId();
        if (!epics.containsKey(epicId)) {
            return null;
        }
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(epicId).addSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        updateEpicTime(subtask.getEpicId());
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            if (hasTimeOverlap(task) && !task.equals(oldTask)) {
                throw new ManagerSaveException("Обновленная задача пересекается по времени с существующей");
            }
            tasks.put(task.getId(), task);
            prioritizedTasks.remove(oldTask);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            int epicId = subtask.getEpicId();
            if (epics.containsKey(epicId) && epics.get(epicId).getSubtaskIds().contains(subtask.getId())) {
                Subtask oldSubtask = subtasks.get(subtask.getId());
                if (hasTimeOverlap(subtask) && !subtask.equals(oldSubtask)) {
                    throw new ManagerSaveException("Обновленная подзадача пересекается по времени с существующей");
                }
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTime(subtask.getEpicId());
                prioritizedTasks.remove(oldSubtask);
                if (subtask.getStartTime() != null) {
                    prioritizedTasks.add(subtask);
                }
            }
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addToHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addToHistory(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addToHistory(subtask);
        }
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubtaskIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void deleteAllTasks() {
        Set<Integer> taskIds = new HashSet<>(tasks.keySet());
        tasks.clear();
        taskIds.forEach(historyManager::removeFromHistory);
    }

    @Override
    public void deleteAllEpics() {
        Set<Integer> allIds = new HashSet<>(epics.keySet());
        epics.values().forEach(epic -> allIds.addAll(epic.getSubtaskIds()));
        epics.clear();
        subtasks.clear();
        allIds.forEach(historyManager::removeFromHistory);
    }

    @Override
    public void deleteAllSubtasks() {
        Set<Integer> subtaskIds = new HashSet<>(subtasks.keySet());
        subtasks.clear();
        subtaskIds.forEach(historyManager::removeFromHistory);
        epics.values().forEach(epic -> {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        });
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.removeFromHistory(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                prioritizedTasks.removeIf(task -> task.getId() == subtaskId);
                historyManager.removeFromHistory(subtaskId);
            }
            historyManager.removeFromHistory(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubtaskId(id);
            prioritizedTasks.remove(subtask);
            updateEpicStatus(epic.getId());
            updateEpicTime(epicId);
            historyManager.removeFromHistory(id);
        }
    }

    protected void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        ArrayList<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
        if (epicSubtasks == null || epicSubtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = epicSubtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.NEW);
        boolean allDone = epicSubtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicTime(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> subtasksList = getSubtasksByEpicId(epicId);
        if (subtasksList == null || subtasksList.isEmpty()) {
            epic.setCalculatedStartTime(null);
            epic.setCalculatedEndTime(null);
            epic.setCalculatedDuration(Duration.ZERO);
            return;
        }

        LocalDateTime earliestStart = subtasksList.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = subtasksList.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        long totalMinutes = subtasksList.stream()
                .filter(subtask -> subtask.getDuration() != null)
                .mapToLong(subtask -> subtask.getDuration().toMinutes())
                .sum();

        // Устанавливаем расчетные значения через специальные методы
        epic.setCalculatedStartTime(earliestStart);
        epic.setCalculatedEndTime(latestEnd);
        epic.setCalculatedDuration(Duration.ofMinutes(totalMinutes));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime());
    }

    public boolean hasTimeOverlap(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(existingTask -> isTasksOverlap(task, existingTask));
    }
}

