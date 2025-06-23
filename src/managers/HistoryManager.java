package managers;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {

   void addToHistory(Task task);

   ArrayList<Task> getHistory();

   void removeFromHistory(int id);

}
