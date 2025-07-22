import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) {

        System.out.println("\nДополнительное задание:");
        Task taskCreate1 = manager.createTask(new Task("Сходить в магазин", "Купить торт для праздника", TaskStatus.NEW, LocalDateTime.of(2025, 5, 15, 14, 30), Duration.ofMinutes(15)));
        Task taskCreate2 = manager.createTask(new Task("Позвонить подруге", "Пригласить подругу на бокал чая", TaskStatus.NEW, LocalDateTime.of(2025, 6, 1, 19, 20), Duration.ofMinutes(45)));
        Epic epicCreate3 = manager.createEpic(new Epic("Финальное задание", "Сделать финальное задание до пятницы"));
        Epic epicCreate4 = manager.createEpic(new Epic("Отпуск", "Отдохнуть на Гавайях"));
        Subtask subtaskCreate5 = manager.createSubtask(new Subtask("Первое", "Погладить кота", TaskStatus.NEW, epicCreate3.getId(), LocalDateTime.of(2025, 7, 19, 10, 30), Duration.ofMinutes(15)));
        Subtask subtaskCreate6 = manager.createSubtask(new Subtask("Второе", "Погладить второго кота", TaskStatus.NEW, epicCreate3.getId(), LocalDateTime.of(2025, 7, 20, 10, 30), Duration.ofMinutes(15)));
        Subtask subtaskCreate7 = manager.createSubtask(new Subtask("Третье", "Создать грандиозное творение", TaskStatus.NEW, epicCreate3.getId(), LocalDateTime.of(2025, 7, 21, 10, 30), Duration.ofMinutes(55)));

        System.out.println("Создали всякие задачи/эпики/подзадачи:");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        System.out.println("\nПриоритетный список задач:");
        manager.getPrioritizedTasks().forEach(task ->
                System.out.printf("%s: %s - %s (длительность: %d мин)%n",
                        task.getStartTime(),
                        task.getName(),
                        task.getEndTime(),
                        task.getDuration().toMinutes())
        );

        System.out.println("\nПодзадачи эпика 3:");
        for (Subtask subtask : manager.getSubtasksByEpicId(epicCreate3.getId())) {
            System.out.printf("%s: start=%s, duration=%s, and=%s%n",
                    subtask.getName(),
                    subtask.getStartTime(),
                    subtask.getDuration(),
                    subtask.getEndTime());
        }

        Epic updatedEpic = manager.getEpicById(epicCreate3.getId());

        System.out.println("\nВремя выполнения эпика:");
        System.out.printf("Начало: %s%n", updatedEpic.getStartTime());
        System.out.printf("Окончание: %s%n", updatedEpic.getEndTime());
        System.out.printf("Общая продолжительность: %d мин%n", updatedEpic.getDuration().toMinutes());


        try {
            Task overlappingTask = new Task("Конфликтная задача", "Должна вызвать ошибку",
                    TaskStatus.NEW, LocalDateTime.of(2025, 5, 15, 14, 30), Duration.ofMinutes(30));
            manager.createTask(overlappingTask);
        } catch (Exception e) {
            System.out.println("\nПроверка пересечения задач:");
            System.out.println("Ошибка при создании задачи: " + e.getMessage());
        }
/*
        System.out.println("\nЗапросили задачи в порядке 1-3-5-2-4-6:");
        manager.getTaskById(taskCreate1.getId());
        manager.getEpicById(epicCreate3.getId());
        manager.getSubtaskById(subtaskCreate5.getId());
        manager.getTaskById(taskCreate2.getId());
        manager.getEpicById(epicCreate4.getId());
        manager.getSubtaskById(subtaskCreate6.getId());

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\nУдалили задачу 2:");
        manager.deleteTaskById(taskCreate2.getId());

        System.out.println("Новая история:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        // Новый сценарий: проверка сохранения/загрузки (доп.задание)
        System.out.println("\nПроверка сохранения и загрузки из файла");
        File file = new File("resources/data.csv");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("\nСравнение задач:");
        System.out.println("Оригинал: " + manager.getAllTasks());
        System.out.println("Загружено: " + loadedManager.getAllTasks());

        System.out.println("\nСравнение эпиков:");
        System.out.println("Оригинал: " + manager.getAllEpics());
        System.out.println("Загружено: " + loadedManager.getAllEpics());

        System.out.println("\nСравнение подзадач:");
        System.out.println("Оригинал: " + manager.getAllSubtasks());
        System.out.println("Загружено: " + loadedManager.getAllSubtasks());

        System.out.println("\nСравнение истории:");
        System.out.println("Оригинал: " + manager.getHistory());
        System.out.println("Загружено: " + loadedManager.getHistory());

        boolean isEqual = manager.getAllTasks().equals(loadedManager.getAllTasks()) &&
                manager.getAllEpics().equals(loadedManager.getAllEpics()) &&
                manager.getAllSubtasks().equals(loadedManager.getAllSubtasks()) &&
                manager.getHistory().equals(loadedManager.getHistory());

        System.out.println("\nРезультат: " + (isEqual ? "ВСЁ СОВПАДАЕТ!" : "Ошибка: данные различаются!")); */
    }
}
