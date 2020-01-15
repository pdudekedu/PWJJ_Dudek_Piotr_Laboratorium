package ToDoManager.UIModels;

import Models.TaskList;

import java.io.Serializable;
import java.util.List;

/**
 * Zapewnia funkcjonalności listy zadań.
 */
public interface ITaskList extends Serializable
{
    /**
     * Pobiera nazwę listy.
     * @return Nazwa listy.
     */
    String GetName();

    /**
     * Pobiera ścieżkę do małej ikony (32x32).
     * @return Ścieżka do małej ikony (32x32).
     */
    String GetSmallIconUrl();

    /**
     * Pobiera ścieżkę do dużej ikony (64x64).
     * @return Ścieżka do dużej ikony (64x64).
     */
    String GetBigIconUrl();
    int GetId();
    int Id = 0;
    boolean IsSuitable(Models.Task task);
}
