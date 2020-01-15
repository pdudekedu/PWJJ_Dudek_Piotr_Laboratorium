package ToDoManager.UIModels;

/**
 * Zapewnia fukncjonalności nasłuchiwania zmian w obiekcie zadania (Task).
 */
public interface ITaskListener
{
    /**
     * Obłsługuje akcję usunięcia zadania.
     * @param sender Zadanie.
     */
    void OnTaskDelete(ITask sender);

    /**
     * Obłsługuje akcję zmiany informacji zadania.
     * @param sender Zadanie.
     */
    void OnTaskUpdate(ITask sender);
}
