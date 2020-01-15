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

public class TaskListsLoading implements Callable<List<ITaskList>>
{
    @Override
    public List<ITaskList> call()
    {
        ArrayList<ITaskList> result = new ArrayList<>();
        try
        {
            Client client = new Client();
            for (TaskList tl: client.GetTaskLists())
            {
                result.add(new ToDoManager.UIModels.CustomTaskList(tl));
            }
            client.Close();

        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się pobrać list.");
        }
        finally
        {
            return result;
        }
    }
}
