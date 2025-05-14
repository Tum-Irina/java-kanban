import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("Let's go!");

        System.out.println("\nСоздание задач:");
        Task taskCreate1 = taskManager.createTask(new Task("Сходить в магазин", "Купить торт для праздника", TaskStatus.NEW));
        Task taskCreate2 = taskManager.createTask(new Task("Позвонить подруге", "Пригласить подругу на бокал чая", TaskStatus.NEW));
        System.out.println("Все задачи:");
        System.out.println(taskManager.getAllTasks());
        Epic epicCreate1 = taskManager.createEpic(new Epic("Финальное задание", "Сделать финальное задание до пятницы"));
        Epic epicCreate2 = taskManager.createEpic(new Epic("Отпуск", "Отдохнуть на Гавайях"));
        System.out.println("Все эпики:");
        System.out.println(taskManager.getAllEpics());
        Subtask subtaskCreate1 = taskManager.createSubtask(new Subtask("Первое", "Погладить кота", TaskStatus.NEW, epicCreate1.getId()));
        Subtask subtaskCreate2 = taskManager.createSubtask(new Subtask("Второе", "Создать грандиозное творение", TaskStatus.NEW, epicCreate1.getId()));
        Subtask subtaskCreate3 = taskManager.createSubtask(new Subtask("To do", "Купить билет на самолет", TaskStatus.NEW, epicCreate2.getId()));
        System.out.println("Все подзадачи:");
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("\nИзменение задачи:");
        Task taskUpdate1 = new Task("Сходить в кондитерскую", "Купить торт для праздника", TaskStatus.DONE);
        taskUpdate1.setId(taskCreate1.getId());
        taskManager.updateTask(taskUpdate1);
        System.out.println("На примере задачи: " + taskManager.getTaskById(taskCreate1.getId()));
        Epic epicUpdate1 = new Epic("Отпуск", "Отдохнуть на Чукотке");
        epicUpdate1.setId(epicCreate2.getId());
        taskManager.updateEpic(epicUpdate1);
        System.out.println("На примере эпика: " + taskManager.getEpicById(epicCreate2.getId()));

        System.out.println("\nПодзадачи эпика:");
        //taskManager.getSubtasksByEpicId(epicCreate1.getId());
        System.out.println("У эпика " + epicCreate1.getId() + " подзадачи: " + taskManager.getSubtasksByEpicId(epicCreate1.getId()));

        System.out.println("\nИзменение статуса:");
        subtaskCreate1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskCreate1);
        subtaskCreate3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskCreate3);
        System.out.println("Изменили статус у Subtask:\n" + taskManager.getAllSubtasks());
        System.out.println("У Task тоже изменился статус:\n" + taskManager.getAllEpics());

        System.out.println("\nУдаление задачи:");
        taskManager.deleteTaskById(taskCreate2.getId());
        System.out.println("Остались задачи:");
        System.out.println(taskManager.getAllTasks());
        taskManager.deleteEpicById(epicCreate2.getId());
        System.out.println("Остались эпики:");
        System.out.println(taskManager.getAllEpics());
        taskManager.deleteSubtask(subtaskCreate2.getEpicId());
        System.out.println("Остались подзадачи:");
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("\nВсё удалить!");
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        System.out.println("Ничего не осталось: " + taskManager.getAllTasks() + taskManager.getAllEpics() + taskManager.getAllSubtasks());
    }
}
