package ToDoManager.UIModels;

import Models.Task;
import ToDoManager.Enum.Images;

public class TodaysTasksList extends TaskList
{
    /**
     * Inicjuje instancję klasy.
     */
    public TodaysTasksList()
    {
        super("Na dzisiaj", Images.TODAY_32, Images.TODAY_64);
    }

    @Override
    public boolean IsSuitable(Task task)
    {
        return task.GetIsPlannedToday();
    }
}
