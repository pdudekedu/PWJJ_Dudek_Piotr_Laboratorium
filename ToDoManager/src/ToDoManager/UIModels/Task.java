package ToDoManager.UIModels;

import ToDoManager.Enum.TaskState;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;

/**
 * Model zadania.
 */
public class Task implements ITask
{
    /**
     * Inicjuje nowe wystąpienie klasy Task.
     * @param task Model zadania.
     */
    public Task(Models.Task task)
    {
        Id = task.Id;
        _Title = task.Title;
        _PlannedStartDate = task.PlannedStartDate;
        _PlannedEndDate = task.PlannedEndDate;
        _IsImportant = task.IsImportant;
        _StartDate = task.StartDate;
        _EndDate = task.EndDate;
        _TaskListId = task.TaskListId;
    }

    /**
     * Inicjuje nowe wystąpienie klasy Task.
     * @param title Tytuł zadania.
     * @param plannedStartDate Planowana data rozpoczęcia.
     * @param plannedEndDate Planowana data zakończenia.
     * @param isImportant Czy zadanie jest oznaczone jako ważne.
     */
    public Task(int taskListId, String title, Date plannedStartDate, Date plannedEndDate, boolean isImportant)
    {
        _Title = title;
        _PlannedStartDate = plannedStartDate;
        _PlannedEndDate = plannedEndDate;
        _IsImportant = isImportant;
        _TaskListId = taskListId;
    }

    /**
     * Inicjuje nowe wystąpienie klasy Task.
     * @param title Tytuł zadania.
     * @param plannedStartDate Planowana data rozpoczęcia.
     * @param plannedEndDate Planowana data zakończenia.
     * @param isImportant Czy zadanie jest oznaczone jako ważne.
     * @param startDate Data rozpoczęcia zadania.
     * @param endDate Data zakońćzenia zadania.
     */
    public Task(int taskListId, String title, Date plannedStartDate, Date plannedEndDate, boolean isImportant, Date startDate, Date endDate)
    {
        this(taskListId, title, plannedStartDate, plannedEndDate, isImportant);
        _StartDate = startDate;
        _EndDate = endDate;
    }

    /**
     * Pobiera tytuł.
     * @return Tytuł.
     */
    @Override
    public String GetTitle() { return _Title; }

    /**
     * Pobiera datę zakończenia zadania.
     * @return Data zakończenia zadania.
     */
    @Override
    public Date GetEndDate() { return _EndDate; }

    /**
     * Pobiera datę rozpoczęcia zadania.
     * @return Data rozpoczęcia zadania.
     */
    @Override
    public Date GetStartDate() { return _StartDate; }

    /**
     * Pobiera planowaną datę rozpoczęcia zadania.
     * @return Planowana data rozpoczęcia zadania.
     */
    @Override
    public Date GetPlannedStartDate() { return _PlannedStartDate; }

    /**
     * Pobiera planowaną datę zakończenia zadania.
     * @return Planowana data zakończenia zadania.
     */
    @Override
    public Date GetPlannedEndDate() { return _PlannedEndDate; }

    /**
     * Pobiera informację, czy zadanie jest oznaczone jako ważne.
     * @return True - jeżeli zadanie jest oznaczone jako ważne.
     */
    @Override
    public  boolean GetIsImportant() { return _IsImportant; }

    /**
     * Pobiera status zadania.
     * @return Stats zadania.
     */
    @Override
    public TaskState GetTaskState()
    {
        if(_EndDate != null)
            return  TaskState.CLOSED;
        else if(_StartDate != null)
            return  TaskState.INPROGRESS;
        else
            return  TaskState.AWAITING;
    }

    /**
     * Ustawia status zadania.
     * @param value Status zadania.
     */
    @Override
    public void SetTaskState(TaskState value)
    {
        switch (value)
        {
            case CLOSED:
                _EndDate = new Date();
                break;
            case AWAITING:
                _EndDate = _StartDate = null;
                break;
            case INPROGRESS:
                _StartDate = new Date();
                break;
        }
        for (ITaskListener listener : _Listeners)
            listener.OnTaskUpdate(this);
    }

    /**
     * Pobiera informację, czy zadanie jest planowane lub wykonywane aktualnego dnia.
     * @return True - jeżeli zadanie jest planowane lub wykonywane aktualnego dnia.
     */
    @Override
    public boolean GetIsPlannedToday()
    {
        Date now = new Date();
        Date toady = new Date(now.getYear(), now.getMonth(), now.getDay(), 0, 0, 0);
        return _EndDate == null && _StartDate != null || _EndDate == null && _PlannedStartDate != null && _PlannedStartDate.after(toady);
    }

    /**
     * Pobiera informację, czy zadanie jest opóźnione..
     * @return True - jeżeli zadanie jest opóźnione.
     */
    @Override public boolean GetIsDelayed()
    {
        Date now = new Date();
        return (_PlannedStartDate != null && _StartDate == null && _PlannedStartDate.before(now)) ||
                (_PlannedEndDate != null && _EndDate == null && _PlannedEndDate.after(now));
    }

    /**
     * Dodaje obiekt nasłuchujący zmian w zadaniu.
     * @param listener Obiekt nasłuchujący.
     */
    @Override
    public void AddListener(ITaskListener listener)
    {
        if(_Listeners == null)
            _Listeners = new ArrayList<>();
        _Listeners.add(listener);
    }

    /**
     * Usuwa obiekt nasłuchujący zmian w zadaniu.
     * @param listener Obiekt nasłuchujący.
     */
    @Override
    public void RemoveListener(ITaskListener listener)
    {
        if(_Listeners == null)
            _Listeners = new ArrayList<>();
        _Listeners.remove(listener);
    }

    /**
     * Wywołuje zdarzenia usunięcia zadnia z listy zadań.
     */
    @Override
    public void OnDelete()
    {
        for (ITaskListener listener : _Listeners)
            listener.OnTaskDelete(this);
    }

    @Override
    public int GetId()
    {
        return Id;
    }

    @Override
    public void Update(Models.Task task)
    {
        _StartDate = task.StartDate;
        _EndDate = task.EndDate;
    }

    public int Id;
    private int _TaskListId;
    /**
     * Tytuł zadania.
     */
    private String _Title = null;
    /**
     * Data zakończenia.
     */
    private Date _EndDate = null;
    /**
     * Data rozpoczęcia.
     */
    private Date _StartDate = null;
    /**
     * Planowana data rozpoczęcia.
     */
    private Date _PlannedStartDate = null;
    /**
     * Planowana data zakończenia.
     */
    private Date _PlannedEndDate = null;
    /**
     * Czy zadanie jest oznaczone jako ważne.
     */
    private boolean _IsImportant = false;
    /**
     * Zarejestrowane obiekty nasłuchujące.
     */
    private transient ArrayList<ITaskListener> _Listeners;

    public Models.Task GetTask()
    {
        Models.Task result = new Models.Task();
        result.Id = Id;
        result.PlannedEndDate = _PlannedEndDate;
        result.EndDate = _EndDate;
        result.IsImportant = _IsImportant;
        result.PlannedStartDate = _PlannedStartDate;
        result.StartDate = _StartDate;
        result.Title = _Title;
        result.TaskListId = _TaskListId;
        return result;
    }
}
