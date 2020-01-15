package ToDoManager.Tasks;

import Models.Task;
import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.ITask;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AllTasksLoading implements Callable<List<ITask>>
{
    public AllTasksLoading()
    {

    }

    @Override
    public List<ITask> call()
    {
        ArrayList<ITask> result = new ArrayList<>();
        try
        {
            Client client = new Client();
            for (Task t: client.GetAllTasks())
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
