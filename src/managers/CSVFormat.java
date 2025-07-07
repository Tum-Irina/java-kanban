package managers;

import tasks.*;

import java.util.List;

public class CSVFormat {

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                getTaskType(task),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task instanceof Subtask ? ((Subtask) task).getEpicId() : "");
    }

    static TaskType getTaskType(Task task) {
        if (task instanceof Epic) return TaskType.EPIC;
        if (task instanceof Subtask) return TaskType.SUBTASK;
        return TaskType.TASK;
    }

    public static String toString(List<Task> history) {
        StringBuilder builder = new StringBuilder();
        for (Task task : history) {
            builder.append(task.getId()).append(",");
        }
        if (!builder.isEmpty()) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public static Task fromString(String line) {
        String[] split = line.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(split[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
            default:
                return null;
        }
    }
}
