package ToDoManager.Controllers;

import ToDoManager.Enum.Images;
import ToDoManager.Enum.TaskState;
import ToDoManager.Main;
import ToDoManager.UIModels.ITask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.Optional;

/**
 * Kontroler dla zadania (ITask).
 */
public class TaskCellController extends ListCell<ITask>
{
    /**
     * Laduje obiekty GUI z pliku FXML.
     */
    private FXMLLoader _Loader;
    /**
     * Główny panel.
     */
    @FXML
    private GridPane _Root;
    /**
     * Przycisk statusu zadania.
     */
    @FXML
    private GridPane _StateButton;
    /**
     * Ikona opóźnienia.
     */
    @FXML
    private GridPane _DelayedIcon;
    /**
     * Ikona oznaczająca zadanie jako ważne.
     */
    @FXML
    private GridPane _ImportantIcon;
    /**
     * Przycisk usunięcia.
     */
    @FXML
    private GridPane _DeleteButton;
    /**
     * Pole tytułu.
     */
    @FXML
    private Text _Title;
    /**
     * Pole daty rozpoczęcia zadania.
     */
    @FXML
    private Text _StartDateLabel;
    /**
     * Pole daty zakończenia zadania.
     */
    @FXML
    private Text _EndDateLabel;
    /**
     * Pole planowanej daty rozpoczęcia zadania.
     */
    @FXML
    private Text _PlannedStartDateLabel;
    /**
     * Pole planowanej daty zakończenia zadania.
     */
    @FXML
    private Text _PlannedEndDateLabel;
    /**
     * Zadanie przypisane do kontrolera.
     */
    private ITask _Task;

    /**
     * Ustawia zawartość elementu ListView na podstawie ITask.
     * @param task Zadanie.
     * @param empty Czy element ListView jest pusty.
     */
    @Override
    protected void updateItem(ITask task, boolean empty) {
        super.updateItem(task, empty);

        _Task = task;
        if(empty || task == null)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            if (_Loader == null) {
                _Loader = new FXMLLoader(Main.Current.getClass().getResource("Views/TaskCell.fxml"));
                _Loader.setController(this);

                try
                {
                    _Loader.load();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            switch (task.GetTaskState())
            {
                case CLOSED:
                    SetStateImage(Images.DONE_32);
                    break;
                case AWAITING:
                    SetStateImage(Images.AWAITING_32);
                    break;
                case INPROGRESS:
                    SetStateImage(Images.IN_PROGRESS_32);
                    break;
            }
            _Title.setText(task.GetTitle());
            _StartDateLabel.setText(task.GetStartDate() != null ? Main.LongDateFormat.format(task.GetStartDate()) : "--");
            _EndDateLabel.setText(task.GetEndDate() != null ? Main.LongDateFormat.format(task.GetEndDate()) : "--");
            _PlannedStartDateLabel.setText(task.GetPlannedStartDate() != null ? Main.LongDateFormat.format(task.GetPlannedStartDate()) : "--");
            _PlannedEndDateLabel.setText(task.GetPlannedEndDate() != null ? Main.LongDateFormat.format(task.GetPlannedEndDate()) : "--");
            _DelayedIcon.setVisible(task.GetIsDelayed());
            _DelayedIcon.setManaged(task.GetIsDelayed());
            _ImportantIcon.setVisible(task.GetIsImportant());
            _ImportantIcon.setManaged(task.GetIsImportant());
            setText(null);
            setGraphic(_Root);
        }
    }

    /**
     * Obsługuje zdarzenie usunięcia zadania.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _DeleteButton_Clicked(MouseEvent e)
    {
        if(_Task != null)
        {
            Optional<ButtonType> result = Main.ShowAlert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć zadanie:" + "\n" + _Task.GetTitle());
            if(result.isPresent() && result.get() == ButtonType.OK)
                _Task.OnDelete();
        }
    }

    /**
     * Obsługuje zdarzenie zmiany stanu zadania.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _StateButton_Clicked(MouseEvent e)
    {
        if(_Task != null)
        {
            switch (_Task.GetTaskState())
            {
                case CLOSED:
                    _Task.SetTaskState(TaskState.AWAITING);
                    SetStateImage(Images.AWAITING_32);
                    break;
                case AWAITING:
                    _Task.SetTaskState(TaskState.INPROGRESS);
                    SetStateImage(Images.IN_PROGRESS_32);
                    break;
                case INPROGRESS:
                    _Task.SetTaskState(TaskState.CLOSED);
                    SetStateImage(Images.DONE_32);
                    break;
            }
        }
    }

    /**
     * Obsługuje zdarzenie najechania kursora myszy na przycisk stanu - zmienia ikonę w zależności od stanu zadania.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _StateButton_Entered(MouseEvent e)
    {
        if(_Task != null)
        {
            switch (_Task.GetTaskState())
            {
                case CLOSED:
                    SetStateImage(Images.AWAITING_32_HOVER);
                    break;
                case AWAITING:
                    SetStateImage(Images.IN_PROGRESS_32_HOVER);
                    break;
                case INPROGRESS:
                    SetStateImage(Images.DONE_32_HOVER);
                    break;
            }
        }
    }

    /**
     * Obsługuje zdarzenie wyjechania kursora myszy z przycisk stanu - zmienia ikonę w zależności od stanu zadania.
     * @param e Argumenty zdarzenia.
     */
    @FXML
    private void _StateButton_Exited(MouseEvent e)
    {
        if(_Task != null)
        {
            switch (_Task.GetTaskState())
            {
                case CLOSED:
                    SetStateImage(Images.DONE_32);
                    break;
                case AWAITING:
                    SetStateImage(Images.AWAITING_32);
                    break;
                case INPROGRESS:
                    SetStateImage(Images.IN_PROGRESS_32);
                    break;
            }
        }
    }

    /**
     * Ustawia ikonę stanu.
     * @param image Ścieżka obrazu.
     */
    private void SetStateImage(String image)
    {
        ((ImageView)_StateButton.getChildren().get(0)).setImage(new Image(Main.Current.getClass().getResourceAsStream(image)));
    }
}
