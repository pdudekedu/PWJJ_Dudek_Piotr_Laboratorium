package ToDoManager.Tasks;

import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.CustomTaskList;
import javafx.scene.control.Alert;

import java.util.concurrent.Callable;

public class TaskListDeleting implements Callable<Void>
{
    public TaskListDeleting(CustomTaskList taskList)
    {
        _TaskList = taskList;
    }

    public CustomTaskList _TaskList;

    @Override
    public Void call()
    {
        try
        {
            Client client = new Client();
            client.DeleteTaskList(_TaskList);
            client.Close();
        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się usunąć listy.");
        }
        finally
        {
            return null;
        }
    }
}
