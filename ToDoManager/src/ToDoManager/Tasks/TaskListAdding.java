package ToDoManager.Tasks;

import Models.TaskList;
import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.CustomTaskList;
import ToDoManager.UIModels.ITaskList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TaskListAdding implements Callable<Void>
{
    public TaskListAdding(CustomTaskList taskList)
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
            client.InsertTaskList(_TaskList);
            client.Close();
        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się dodać listy.");
        }
        finally
        {
            return null;
        }
    }
}
