package ToDoManager.Tasks;

import Models.SyncData;
import Models.Task;
import ToDoManager.Clients.Client;
import ToDoManager.Main;
import ToDoManager.UIModels.ITask;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SynchronizationTask implements Callable<SyncData>
{
    public SynchronizationTask()
    {

    }

    @Override
    public SyncData call()
    {
        SyncData result = null;
        try
        {
            Client client = new Client();
            result = client.Sync();
            client.Close();

        } catch (Exception ex)
        {
            Main.ShowAlert(Alert.AlertType.ERROR, "Nie udało się pobrać zadań.");
            System.out.println(ex.getStackTrace());
        }
        finally
        {
            return result;
        }
    }
}
