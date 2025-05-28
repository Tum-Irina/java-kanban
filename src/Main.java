import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) {
        System.out.println("Let's go!");

        System.out.println("\nСоздание задач:");
        Task taskCreate1 = manager.createTask(new Task("Сходить в магазин", "Купить торт для праздника", TaskStatus.NEW));
        Task taskCreate2 = manager.createTask(new Task("Позвонить подруге", "Пригласить подругу на бокал чая", TaskStatus.NEW));
        System.out.println("Все задачи:");
        System.out.println(manager.getAllTasks());
        Epic epicCreate1 = manager.createEpic(new Epic("Финальное задание", "Сделать финальное задание до пятницы"));
        Epic epicCreate2 = manager.createEpic(new Epic("Отпуск", "Отдохнуть на Гавайях"));
        System.out.println("Все эпики:");
        System.out.println(manager.getAllEpics());
        Subtask subtaskCreate1 = manager.createSubtask(new Subtask("Первое", "Погладить кота", TaskStatus.NEW, epicCreate1.getId()));
        Subtask subtaskCreate2 = manager.createSubtask(new Subtask("Второе", "Создать грандиозное творение", TaskStatus.NEW, epicCreate1.getId()));
        Subtask subtaskCreate3 = manager.createSubtask(new Subtask("To do", "Купить билет на самолет", TaskStatus.NEW, epicCreate2.getId()));
        System.out.println("Все подзадачи:");
        System.out.println(manager.getAllSubtasks());

        manager.getTaskById(taskCreate1.getId());
        manager.getEpicById(epicCreate1.getId());
        manager.getSubtaskById(subtaskCreate1.getId());

        System.out.println("\nИзменение задачи:");
        Task taskUpdate1 = new Task("Сходить в кондитерскую", "Купить торт для праздника", TaskStatus.DONE);
        taskUpdate1.setId(taskCreate1.getId());
        manager.updateTask(taskUpdate1);
        System.out.println("На примере задачи: " + manager.getTaskById(taskCreate1.getId()));
        Epic epicUpdate1 = new Epic("Отпуск", "Отдохнуть на Чукотке");
        epicUpdate1.setId(epicCreate2.getId());
        manager.updateEpic(epicUpdate1);
        System.out.println("На примере эпика: " + manager.getEpicById(epicCreate2.getId()));

        System.out.println("\nИстория:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\nПодзадачи эпика:");
        manager.getSubtasksByEpicId(epicCreate1.getId());
        System.out.println("Подзадачи у эпика " + epicCreate1.getId() + ": " + manager.getSubtasksByEpicId(epicCreate1.getId()));

        System.out.println("\nИзменение статуса:");
        subtaskCreate1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtaskCreate1);
        subtaskCreate3.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtaskCreate3);
        System.out.println("Изменили статус у Subtask:\n" + manager.getAllSubtasks());
        System.out.println("У Task тоже изменился статус:\n" + manager.getAllEpics());

        System.out.println("\nУдаление задачи:");
        manager.deleteTaskById(taskCreate2.getId());
        System.out.println("Остались задачи:");
        System.out.println(manager.getAllTasks());
        manager.deleteEpicById(epicCreate2.getId());
        System.out.println("Остались эпики:");
        System.out.println(manager.getAllEpics());
        manager.deleteSubtask(subtaskCreate2.getEpicId());
        System.out.println("Остались подзадачи:");
        System.out.println(manager.getAllSubtasks());

        System.out.println("\nВсё удалить!");
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        System.out.println("Ничего не осталось: " + manager.getAllTasks() + manager.getAllEpics() + manager.getAllSubtasks());
    }
}
