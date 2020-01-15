package ToDoManager.Tasks;

import Models.Task;
import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.ITask;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TasksLoading implements Callable<List<ITask>>
{
    private final int _TaskListId;

    public TasksLoading(int taskListId)
    {
        this._TaskListId = taskListId;
    }

    @Override
    public List<ITask> call()
    {
        ArrayList<ITask> result = new ArrayList<>();
        try
        {
            Client client = new Client();
            for (Task t: client.GetTasks(_TaskListId))
            {
                result.add(new ToDoManager.UIModels.Task(t));
            }
            client.Close();

        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się pobrać zadań.");
        }
        finally
        {
            return result;
        }
    }
}
