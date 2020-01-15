package ToDoManager.UIModels;

import Models.Task;
import ToDoManager.Enum.Images;

public class ImportantTasksList extends TaskList
{
    /**
     * Inicjuje instancję klasy.
     */
    public ImportantTasksList()
    {
        super("Ważne", Images.IMPORTANT_32, Images.IMPORTANT_64);
    }

    @Override
    public boolean IsSuitable(Task task)
    {
        return task.GetIsImportant();
    }
}
