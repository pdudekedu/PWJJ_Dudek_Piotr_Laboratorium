package ToDoManager.Tasks;

import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.CustomTaskList;
import ToDoManager.UIModels.Task;
import javafx.scene.control.Alert;

import java.util.concurrent.Callable;

public class TaskAdding implements Callable<Void>
{
    public TaskAdding(Task taskList)
    {
        _Task = taskList;
    }

    public Task _Task;

    @Override
    public Void call()
    {
        try
        {
            Client client = new Client();
            client.InsertTask(_Task);
            client.Close();
        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się dodać zadania.");
        }
        finally
        {
            return null;
        }
    }
}
