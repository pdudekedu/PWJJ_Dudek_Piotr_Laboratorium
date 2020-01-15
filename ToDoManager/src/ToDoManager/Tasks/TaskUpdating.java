package ToDoManager.Tasks;

import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.Task;
import javafx.scene.control.Alert;

import java.util.concurrent.Callable;

public class TaskUpdating implements Callable<Void>
{
    public TaskUpdating(Task taskList)
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
            client.UpdateTask(_Task);
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
