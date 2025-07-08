import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

public class Main {

    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) {

        System.out.println("\nДополнительное задание:");
        Task taskCreate1 = manager.createTask(new Task("Сходить в магазин", "Купить торт для праздника", TaskStatus.NEW));
        Task taskCreate2 = manager.createTask(new Task("Позвонить подруге", "Пригласить подругу на бокал чая", TaskStatus.NEW));
        Epic epicCreate3 = manager.createEpic(new Epic("Финальное задание", "Сделать финальное задание до пятницы"));
        Epic epicCreate4 = manager.createEpic(new Epic("Отпуск", "Отдохнуть на Гавайях"));
        Subtask subtaskCreate5 = manager.createSubtask(new Subtask("Первое", "Погладить кота", TaskStatus.NEW, epicCreate3.getId()));
        Subtask subtaskCreate6 = manager.createSubtask(new Subtask("Второе", "Погладить второго кота", TaskStatus.NEW, epicCreate3.getId()));
        Subtask subtaskCreate7 = manager.createSubtask(new Subtask("Третье", "Создать грандиозное творение", TaskStatus.NEW, epicCreate3.getId()));

        System.out.println("Создали всякие задачи/эпики/подзадачи:");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

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

        System.out.println("\nРезультат: " + (isEqual ? "ВСЁ СОВПАДАЕТ!" : "Ошибка: данные различаются!"));
    }
}
