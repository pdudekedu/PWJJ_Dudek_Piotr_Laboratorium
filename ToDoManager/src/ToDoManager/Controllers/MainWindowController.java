package ToDoManager.Controllers;

import Models.SyncData;
import ToDoManager.Enum.Images;
import ToDoManager.Main;
import ToDoManager.UIModels.*;
import ToDoManager.Tasks.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import sun.util.resources.cldr.ti.CurrencyNames_ti;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Kontroler głównego okna aplikacji.
 */
public class MainWindowController implements ITaskListener
{
    /**
     * Inicjuje instancję klasy MainWindowController.
     */
    public  MainWindowController()
    {
        Current = this;
        _CustomTaskLists.addAll(ExecuteTask(new TaskListsLoading()));
        _OverviewTaskLists.addAll(_TodayTaskList, _ImportantTaskList, _AllTaskList);
        _Tasks.clear();

        _Timer = new Timer();
        _Timer.scheduleAtFixedRate(new SyncTask(), 5*1000, 10*1000);
    }

    private  Timer _Timer;

    class SyncTask extends TimerTask
    {
        public void run() {
            SyncData syncData = ExecuteTask(new SynchronizationTask());
            if(syncData != null)
            {
                if(!syncData.InsertedTasksLists.isEmpty())
                {
                    synchronized (_CustomTaskLists)
                    {
                        Platform.runLater(() -> {
                            for (Models.TaskList t: syncData.InsertedTasksLists)
                                _CustomTaskLists.add(new ToDoManager.UIModels.CustomTaskList(t));
                        });
                    }
                }
                if(!syncData.DeletedTasksLists.isEmpty())
                {
                    synchronized (_CustomTaskLists)
                    {
                        Platform.runLater(() -> {
                            for (Integer id: syncData.DeletedTasksLists)
                            {
                                ITaskList item = _CustomTaskLists.stream().filter(x -> x.GetId() == id).findAny().orElse(null);
                                if(item != null)
                                {
                                    if(_SelectedTaskList == item)
                                        _SelectTaskList(null);
                                    _CustomTaskLists.remove(item);
                                }
                            }
                        });
                    }
                }
                if(_SelectedTaskList != null && !syncData.InsertedTasks.isEmpty())
                {
                    synchronized (_Tasks)
                    {
                        Platform.runLater(() -> {
                            for (Models.Task t: syncData.InsertedTasks.stream().filter(x -> _SelectedTaskList.IsSuitable(x)).collect(Collectors.toList()))
                                _Tasks.add(new ToDoManager.UIModels.Task(t));
                        });
                    }
                }
                if(!syncData.UpdatedTasks.isEmpty())
                {
                    synchronized (_Tasks)
                    {
                        Platform.runLater(() -> {
                            for (Models.Task t: syncData.UpdatedTasks)
                            {
                                ITask item = _Tasks.stream().filter(x -> x.GetId() == t.GetId()).findAny().orElse(null);
                                if(item != null)
                                    item.Update(t);
                            }
                            _TasksListView.refresh();
                        });
                    }
                }
                if(!syncData.DeletedTasks.isEmpty())
                {
                    synchronized (_Tasks)
                    {
                        Platform.runLater(() -> {
                            for (Integer id: syncData.DeletedTasks)
                            {
                                ITask item = _Tasks.stream().filter(x -> x.GetId() == id).findAny().orElse(null);
                                if(item != null)
                                    _Tasks.remove(item);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Aktualnie działająca instancja głównego okna aplikacji.
     */
    public static MainWindowController Current;

    /**
     * Ikona aktualnie wybranej listy zadań.
     */
    @FXML
    private ImageView _CurrentListIcon;
    /**
     * Etykieta nazwy aktualnie wybranej listy zadań.
     */
    @FXML
    private Text _CurrentListName;
    /**
     * Pole tekstowe nazwy nowej listy zadań.
     */
    @FXML
    private TextField _ListNameInput;
    /**
     * Panel dodawania nowego zadania.
     */
    @FXML
    private GridPane _TaskPane;
    /**
     * Pole tekstowe nazwy nowego zadania.
     */
    @FXML
    private TextField _TaskNameInput;
    /**
     * Pole tekstowe planowanej daty rozpoczęcia zadania.
     */
    @FXML
    private TextField _TaskDateFromInput;
    /**
     * Pole tekstowe planowanej daty zakońćzenia zadania.
     */
    @FXML
    private TextField _TaskDateToInput;
    /**
     * Lista wyświetlająca listy zadań użytkownika.
     */
    @FXML
    private ListView<ITaskList> _CustomTaskListView;
    /**
     * Lista wyświetlająca agregujące listy zadań.
     */
    @FXML
    private ListView<ITaskList> _OverviewTaskListView;
    /**
     * Lista wyświetlająca zadania.
     */
    @FXML
    private ListView<ITask> _TasksListView;
    /**
     * Przycisk usunięca aktualnie wybranej listy.
     */
    @FXML
    private GridPane _CurrentListDeleteButton;
    @FXML
    /**
     * Przycisk dodania nowego zadania.
     */
    private GridPane _AddTaskIsImportantButton;

    /**
     * Pobiera kontener obrazu przycisku usunięca aktualnie wybranej listy.
     * @return Kontener obrazu przycisku usunięca aktualnie wybranej listy.
     */
    private ImageView _GetCurrentListDeleteButtonImageView() {return ((ImageView)_CurrentListDeleteButton.getChildren().get(0)); }

    /**
     * Wybrana lista zadań.
     */
    private ITaskList _SelectedTaskList = null;
    /**
     * Stan przycisku określającego, czy nowe zadanie ma być oznaczone jako ważne.
     */
    private boolean _IsImportantChecked = false;
    /**
     * Lista list zadań użytkownika.
     */
    public final ObservableList<ITaskList> _CustomTaskLists = FXCollections.observableArrayList();
    /**
     * Lista agregowalnych list zadań.
     */
    public final ObservableList<ITaskList> _OverviewTaskLists = FXCollections.observableArrayList();
    /**
     * Lista zadań aktualnie wybranej listy.
     */
    public final ObservableList<ITask> _Tasks = FXCollections.observableArrayList();
    /**
     * Lista zadań "Na dzisiaj".
     */
    public final ITaskList _TodayTaskList = new TodaysTasksList();
    /**
     * Lista zadań "Ważne".
     */
    public final ITaskList _ImportantTaskList = new ImportantTasksList();
    /**
     * Lista zadań "Wszystkie".
     */
    public final ITaskList _AllTaskList = new AllTasksList();

    private <TTask extends Callable<TResult>, TResult> TResult ExecuteTask(TTask loader)
    {
        try
        {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<TResult> future = executorService.submit(loader);
            TResult result;
            result = future.get();
            executorService.shutdown();
            return result;
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda inicjalizująca elementy GUI po ich porzypisaniu do pól.
     */
    @FXML
    private void initialize()
    {
        _SelectTaskList(_TodayTaskList);
        _GetCurrentListDeleteButtonImageView().setImage(new Image(Main.Current.getClass().getResourceAsStream(Images.DELETE_64)));
        _CustomTaskListView.setCellFactory((Callback<ListView<ITaskList>, ListCell<ITaskList>>) list -> new TaskListCellController());
        _CustomTaskListView.setItems(_CustomTaskLists);
        _OverviewTaskListView.setCellFactory((Callback<ListView<ITaskList>, ListCell<ITaskList>>) list -> new TaskListCellController());
        _OverviewTaskListView.setItems(_OverviewTaskLists);
        _TasksListView.setCellFactory((Callback<ListView<ITask>, ListCell<ITask>>) list -> new TaskCellController());
        _TasksListView.setItems(_Tasks);

        _TaskPane.managedProperty().bind(_TaskPane.visibleProperty());
    }

    /**
     * Obsługuje zdarzenia klikniącia przycisku dodania nowej listy.
     */
    @FXML
    private void _AddNewListButton_Clicked()
    {
        _AddNewTaskList();
    }

    /**
     * Obłsuguje zdarzenie przyciśnięcia przycisku klawiatury w polu tekstowym nazwy nowej listy. Dodaje nową listę przy kliknięciu ENTER.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void  _ListNameInput_KeyPressed(KeyEvent e)
    {
        if(e.getCode() == KeyCode.ENTER)
            _AddNewTaskList();
    }

    /**
     * Obłsuguje zdarzenie kliknięcia elementu ListView listy zadań użytkownika.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _CustomTaskListView_Clicked(MouseEvent e)
    {
        ITaskList item = _CustomTaskListView.getSelectionModel().getSelectedItem();
        if(item != null)
            _SelectTaskList(item);
    }

    /**
     * Obłsuguje zdarzenie kliknięcia elementu ListView listy zadań agregowalnych.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _OverviewTaskListView_Clicked(MouseEvent e)
    {
        ITaskList item = _OverviewTaskListView.getSelectionModel().getSelectedItem();
        if(item != null)
            _SelectTaskList(item);
    }

    /**
     * Obłsuguje zdarzenie kliknięcia przycisku usunięcia aktualnie wybranej listy zadań.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _CurrentListDeleteButton_Clicked(MouseEvent e)
    {
        if(_SelectedTaskList != null && _SelectedTaskList instanceof CustomTaskList)
        {
            synchronized (_CustomTaskLists)
            {
                Optional<ButtonType> alertResult = Main.ShowAlert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć listę " + _SelectedTaskList.GetName() + "?");
                if (alertResult.isPresent() && alertResult.get().equals(ButtonType.OK))
                {
                    ExecuteTask(new TaskListDeleting(((CustomTaskList) _SelectedTaskList)));
                    _CustomTaskLists.remove(_SelectedTaskList);
                    _SelectTaskList(null);
                }
            }
        }
    }

    /**
     * Obłsuguje zdarzenie kliknięcia przucisku oznaczenia nowego zadania jako ważne.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _AddTaskIsImportantButton_Clicked(MouseEvent e)
    {
        _IsImportantChecked = !_IsImportantChecked;
        if(_IsImportantChecked)
            ((ImageView)_AddTaskIsImportantButton.getChildren().get(0)).setImage(new Image(Main.Current.getClass().getResourceAsStream(Images.IMPORTANT_32_ENABLED)));
        else
            ((ImageView)_AddTaskIsImportantButton.getChildren().get(0)).setImage(new Image(Main.Current.getClass().getResourceAsStream(Images.IMPORTANT_32_DISABLED)));
    }

    /**
     * Obłsuguje zdarzenie klinięcia przycisku dodania nowego zadania.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _AddTaskButton_Clicked(MouseEvent e)
    {
        _AddNewTask();
    }

    /**
     * Obłsuguje zdarzenie przyciśnięcia przycisku klawiatury w polu tekstowym tytyłu nowego zadania. Dodaje nowe zadanie przy kliknięciu ENTER.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _TaskNameInput_KeyPressed(KeyEvent e)
    {
        if(e.getCode() == KeyCode.ENTER)
            _AddNewTask();
    }

    /**
     * Obsługuje zdarzenie usunięcia zadania.
     * @param sender Zadanie.
     */
    @Override
    public void OnTaskDelete(ITask sender)
    {
        _Tasks.remove(sender);
        ExecuteTask(new TaskDeleting((Task)sender));
        sender.RemoveListener(this);
    }

    /**
     * Obsługuje zdarzenie zmiany informacji zadania. Przeładowuje listę zadań.
     * @param sender Zadanie.
     */
    @Override
    public void OnTaskUpdate(ITask sender)
    {
        ExecuteTask(new TaskUpdating((Task)sender));
        _TasksListView.refresh();
    }

    /**
     * Ustawia elementy dla nowo wybranej listy zadań.
     * @param taskList Nowo wybrana lista zadań.
     */
    private void _SelectTaskList(ITaskList taskList)
    {
        if(taskList == null)
        {
            _SelectedTaskList = null;
            _Tasks.clear();
            _IsImportantChecked = false;
            _CurrentListIcon.setImage(null);
            _CurrentListName.setText(null);
            _CurrentListDeleteButton.setVisible(false);
            _TaskPane.setVisible(false);
        }
        else
        {
            _SelectedTaskList = taskList;
            List<ITask> tasks = new ArrayList<>();
            if(_SelectedTaskList instanceof CustomTaskList)
                tasks = ExecuteTask(new TasksLoading(((CustomTaskList)taskList).Id));
            else if(_SelectedTaskList instanceof AllTasksList)
                tasks = ExecuteTask(new AllTasksLoading());
            else if(_SelectedTaskList instanceof ImportantTasksList)
                tasks = ExecuteTask(new ImportantTasksLoading());
            else if(_SelectedTaskList instanceof TodaysTasksList)
                tasks = ExecuteTask(new TodaysTasksLoading());

            for(ITask task : tasks)
                task.AddListener(this);

            synchronized (_Tasks)
            {
                _Tasks.clear();
                _Tasks.addAll(tasks);
            }

            _IsImportantChecked = false;
            _CurrentListIcon.setImage(new Image(Main.Current.getClass().getResourceAsStream(taskList.GetBigIconUrl())));
            _CurrentListName.setText(taskList.GetName());
            _CurrentListDeleteButton.setVisible(_SelectedTaskList instanceof CustomTaskList);
            _TaskPane.setVisible(_SelectedTaskList instanceof CustomTaskList);
        }
    }

    /**
     * Dodaje nową listę zadań.
     */
    private void _AddNewTaskList()
    {
        synchronized (_CustomTaskLists)
        {
            String newListName = _ListNameInput.getText().trim();
            if (newListName.isEmpty())
                Main.ShowAlert(Alert.AlertType.WARNING, "Nazwa nowej listy nie może być pusta!");
            else if (_CustomTaskLists.stream().anyMatch(x -> x.GetName().equals(newListName)))
                Main.ShowAlert(Alert.AlertType.WARNING, "Istnieje już lista o takiej nazwie!");
            else
            {
                CustomTaskList newList = new CustomTaskList(newListName);
                ExecuteTask(new TaskListAdding(newList));
                _CustomTaskLists.add(newList);
                _ListNameInput.clear();
            }
        }
    }

    /**
     * Dodaje nowe zadanie.
     */
    private void _AddNewTask()
    {
        if(_SelectedTaskList == null || !(_SelectedTaskList instanceof CustomTaskList))
            return;

        String title = _TaskNameInput.getText().trim();
        Date from = null, to = null;
        if(title.isEmpty())
        {
            Main.ShowAlert(Alert.AlertType.WARNING, "Nazwa nowego zadania nie może być pusta!");
            return;
        }
        if(!_TaskDateFromInput.getText().isEmpty())
        {
            String fromString = _TaskDateFromInput.getText().trim();
            try
            {
                from = Main.LongDateFormat.parse(fromString);
            } catch (ParseException ex)
            {
                Main.ShowAlert(Alert.AlertType.WARNING, "Data planowanego rozpoczęcia jest w niepoprawnym formacie!");
                return;
            }
        }
        if(!_TaskDateToInput.getText().isEmpty())
        {
            String toString = _TaskDateToInput.getText().trim();
            try
            {
                to = Main.LongDateFormat.parse(toString);
            } catch (ParseException ex)
            {
                Main.ShowAlert(Alert.AlertType.WARNING, "Data planowanego zakończenia jest w niepoprawnym formacie!");
                return;
            }
        }

        synchronized (_Tasks)
        {
            Task newTask = new Task(((CustomTaskList) _SelectedTaskList).Id, title, from, to, _IsImportantChecked);
            ExecuteTask(new TaskAdding(newTask));
            newTask.AddListener(this);
            _Tasks.add(newTask);
            _ProcessNewTask(newTask);
        }

        _TaskNameInput.clear();
        _TaskDateFromInput.clear();
        _TaskDateToInput.clear();
        _IsImportantChecked = false;
        ((ImageView)_AddTaskIsImportantButton.getChildren().get(0)).setImage(new Image(Main.Current.getClass().getResourceAsStream(Images.IMPORTANT_32_DISABLED)));
    }

    /**
     * Ustawia zadanie do odpowiednich list agregowanych.
     * @param task Zadanie.
     */
    public void _ProcessNewTask(ITask task)
    {

    }
}
