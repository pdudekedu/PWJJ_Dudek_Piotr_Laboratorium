package ToDoManager.UIModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Model dla listy zadań.
 */
public abstract class TaskList implements ITaskList
{
    /**
     * Inicjuje instancję klasy TaskList.
     * @param name Nazwa listy.
     * @param smallIconUrl Ścieżka do małej ikony (32x32).
     * @param bigIconUrl Ścieżka do dużej ikony (64x64).
     */
    public TaskList(String name, String smallIconUrl, String bigIconUrl)
    {
        _Name = name;
        _SmallIconUrl = smallIconUrl;
        _BigIconUrl = bigIconUrl;
    }

    public int Id = 0;

    public int GetId() {return Id; }

    /**
     * Pobiera nazwę listy.
     * @return Nazwa listy.
     */
    @Override
    public String GetName() { return _Name; }

    /**
     * Pobiera ścieżkę do małej ikony (32x32).
     * @return Ścieżka do małej ikony (32x32).
     */
    @Override
    public String GetSmallIconUrl() { return _SmallIconUrl; }

    /**
     * Pobiera ścieżkę do dużej ikony (64x64).
     * @return Ścieżka do dużej ikony (64x64).
     */
    @Override
    public String GetBigIconUrl() { return _BigIconUrl; }

    /**
     * Nazwa listy.
     */
    private String _Name;
    /**
     * Ścieżka do małej ikony (32x32).
     */
    private String _SmallIconUrl;
    /**
     * Ścieżka do dużej ikony (64x64).
     */
    private String _BigIconUrl;
}
