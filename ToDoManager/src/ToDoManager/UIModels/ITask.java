package ToDoManager.UIModels;

import ToDoManager.Enum.TaskState;
import java.io.Serializable;
import java.util.Date;

/**
 * Zapewnia funkcjonalności zadania.
 */
public interface ITask extends Serializable
{
    /**
     * Pobiera tytuł.
     * @return Tytuł.
     */
    String GetTitle();

    /**
     * Pobiera datę zakończenia zadania.
     * @return Data zakończenia zadania.
     */
    Date GetEndDate();

    /**
     * Pobiera datę rozpoczęcia zadania.
     * @return Data rozpoczęcia zadania.
     */
    Date GetStartDate();

    /**
     * Pobiera planowaną datę rozpoczęcia zadania.
     * @return Planowana data rozpoczęcia zadania.
     */
    Date GetPlannedStartDate();

    /**
     * Pobiera planowaną datę zakończenia zadania.
     * @return Planowana data zakończenia zadania.
     */
    Date GetPlannedEndDate();

    /**
     * Pobiera status zadania.
     * @return Stats zadania.
     */
    TaskState GetTaskState();

    /**
     * Ustawia status zadania.
     * @param value Status zadania.
     */
    void SetTaskState(TaskState value);

    /**
     * Pobiera informację, czy zadanie jest oznaczone jako ważne.
     * @return True - jeżeli zadanie jest oznaczone jako ważne.
     */
    boolean GetIsImportant();

    /**
     * Pobiera informację, czy zadanie jest planowane lub wykonywane aktualnego dnia.
     * @return True - jeżeli zadanie jest planowane lub wykonywane aktualnego dnia.
     */
    boolean GetIsPlannedToday();

    /**
     * Pobiera informację, czy zadanie jest opóźnione..
     * @return True - jeżeli zadanie jest opóźnione.
     */
    boolean GetIsDelayed();

    /**
     * Dodaje obiekt nasłuchujący zmian w zadaniu.
     * @param listener Obiekt nasłuchujący.
     */
    void AddListener(ITaskListener listener);

    /**
     * Usuwa obiekt nasłuchujący zmian w zadaniu.
     * @param listener Obiekt nasłuchujący.
     */
    void RemoveListener(ITaskListener listener);

    /**
     * Wywołuje zdarzenia usunięcia zadnia z listy zadań.
     */
    void OnDelete();
    int GetId();
    void Update(Models.Task task);
}
