package ToDoManager.Tasks;

import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.CustomTaskList;
import ToDoManager.UIModels.Task;
import javafx.scene.control.Alert;

import java.util.concurrent.Callable;

public class TaskDeleting implements Callable<Void>
{
    public TaskDeleting(Task taskList)
    {
        _TaskList = taskList;
    }

    public Task _TaskList;

    @Override
    public Void call()
    {
        try
        {
            Client client = new Client();
            client.DeleteTask(_TaskList);
            client.Close();
        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się usunąć zadania.");
        }
        finally
        {
            return null;
        }
    }
}
