package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, null, null);
        this.subtaskIds = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getInternalEndTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        throw new UnsupportedOperationException("Время эпика рассчитывается автоматически на основе подзадач");
    }

    @Override
    public void setDuration(Duration duration) {
        throw new UnsupportedOperationException("Длительность эпика рассчитывается автоматически на основе подзадач");
    }

    public void setCalculatedStartTime(LocalDateTime startTime) {
        super.setInternalStartTime(startTime);
    }

    public void setCalculatedEndTime(LocalDateTime endTime) {
        super.setInternalEndTime(endTime);
    }

    public void setCalculatedDuration(Duration duration) {
        super.setInternalDuration(duration);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("Task", "Epic") +
                ", subtaskIds=" + subtaskIds + '}';
    }
}
