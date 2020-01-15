package ToDoManager.UIModels;

import Models.Task;
import ToDoManager.Enum.Images;

/**
 * Zapewnia funkcjonalności listy zadań użytkownika.
 */
public class CustomTaskList extends TaskList
{
    /**
     * Inicjuje instancję klasy CustomTaskList.
     * @param name Nazwa listy.
     */
    public CustomTaskList(String name)
    {
        super(name, Images.TO_DO_LIST_32, Images.TO_DO_LIST_64);
    }

    public CustomTaskList(Models.TaskList tl)
    {
        super(tl.Name, Images.TO_DO_LIST_32, Images.TO_DO_LIST_64);
        Id = tl.Id;
    }

    public Models.TaskList GetTaskList()
    {
        Models.TaskList result = new Models.TaskList();
        result.Id = Id;
        result.Name = GetName();
        return result;
    }

    @Override
    public boolean IsSuitable(Task task)
    {
        return task.GetTaskListId() == Id;
    }
}
