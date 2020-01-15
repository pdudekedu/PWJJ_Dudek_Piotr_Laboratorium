package ToDoManager.UIModels;

import Models.Task;
import ToDoManager.Enum.Images;

public class AllTasksList extends TaskList
{
    /**
     * Inicjuje instancję klasy.
     */
    public AllTasksList()
    {
        super("Wszystkie", Images.ALL_32, Images.ALL_64);
    }

    @Override
    public boolean IsSuitable(Task task)
    {
        return true;
    }
}
