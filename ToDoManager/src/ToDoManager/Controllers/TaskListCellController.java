package ToDoManager.Controllers;

import ToDoManager.Main;
import ToDoManager.UIModels.ITaskList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.IOException;

/**
 * Kontroler dla listy zadań (ITaskList).
 */
public class TaskListCellController extends ListCell<ITaskList>
{
    /**
     * Laduje obiekty GUI z pliku FXML.
     */
    private FXMLLoader _Loader;
    @FXML
    /**
     * Główny panel.
     */
    private GridPane _Root;
    @FXML
    /**
     * Element przechowujący ikonę listy.
     */
    private ImageView _Icon;
    @FXML
    /**
     * Etykieta przechowująca nazwę listy.
     */
    private Label _Name;

    /**
     * Ustawia zawartość elementu ListView na podstawie ITaskList.
     * @param taskList Lista zadań.
     * @param empty Czy element ListView jest pusty.
     */
    @Override
    protected void updateItem(ITaskList taskList, boolean empty) {
        super.updateItem(taskList, empty);

        if(empty || taskList == null)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            if (_Loader == null) {
                _Loader = new FXMLLoader(Main.Current.getClass().getResource("Views/TaskListCell.fxml"));
                _Loader.setController(this);

                try
                {
                    _Loader.load();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            _Name.setText(taskList.GetName());
            _Name.setTooltip(new Tooltip(taskList.GetName()));
            _Icon.setImage(new Image(Main.Current.getClass().getResourceAsStream(taskList.GetSmallIconUrl())));

            setText(null);
            setGraphic(_Root);
        }
    }
}
