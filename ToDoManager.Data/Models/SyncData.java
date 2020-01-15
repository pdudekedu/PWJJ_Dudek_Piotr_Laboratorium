package Models;

import java.util.ArrayList;
import java.util.List;

public class SyncData
{
    public List<Task> InsertedTasks = new ArrayList<>();
    public List<Task> UpdatedTasks = new ArrayList<>();
    public List<Integer> DeletedTasks = new ArrayList<>();
    public List<TaskList> InsertedTasksLists = new ArrayList<>();
    public List<Integer> DeletedTasksLists = new ArrayList<>();
}
