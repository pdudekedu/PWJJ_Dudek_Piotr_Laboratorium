package ToDoManager;

import ToDoManager.Controllers.MainWindowController;
import ToDoManager.Enum.Images;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

/**
 * Zarządza uruchomieniem i zakończeniem działania aplikacji.
 */
public class Main extends Application {
    /**
     * Uruchamia główne okno aplikacji.
     * @param primaryStage Kontener zawartości okna.
     * @throws Exception Wyjątek działania aplikacji.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Current = this;
        Parent root = FXMLLoader.load(getClass().getResource("Views/MainWindow.fxml"));
        SetProgramIcon(primaryStage);
        primaryStage.setTitle(ProgramTitle);
        primaryStage.setMaximized(true);
        primaryStage.setHeight(800);
        primaryStage.setWidth(1200);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add("Styles.css");
        primaryStage.show();
    }

    private final UUID _ApplicationId = UUID.randomUUID();

    public UUID GetApplicationId() { return  _ApplicationId; }

    /**
     * Kończy działanie aplikacji. Odpowiada za wywołanie serializacji danych do pliku.
     */
    @Override
    public void stop()
    {

    }

    /**
     * Rozpoczyna działanie apliakcji.
     * @param args Argumenty aplikacji.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Ustawia ikonę programu dla podanego kontenera.
     * @param stage Kontener.
     */
    public static void SetProgramIcon(final Stage stage)
    {
        stage.getIcons().add(new Image(Current.getClass().getResourceAsStream(Images.PROGRAM_ICON)));
    }

    /**
     * Ustawia ikonę programu dla podanego okna dialogowego.
     * @param alert Okno dialogowe.
     */
    public static void SetProgramIcon(final Alert alert)
    {
        SetProgramIcon((Stage)alert.getDialogPane().getScene().getWindow());
    }

    /**
     * Wyświetla okno wiadomości.
     * @param type Typ wiadomości.
     * @param text Tekst wiadomości.
     * @param header Nagłówek.
     * @return Wybrany przez użytkownika przycisk.
     */
    public static Optional<ButtonType> ShowAlert(final Alert.AlertType type, final String text, final String header)
    {
        Alert alert = new Alert(type);
        Main.SetProgramIcon(alert);
        alert.setTitle(Main.ProgramTitle);
        alert.setHeaderText(header);
        alert.setContentText(text);
        return alert.showAndWait();
    }

    /**
     * Wyświetla okno wiadomości.
     * @param type Typ wiadomości.
     * @param text Tekst wiadomości.
     * @return Wybrany przez użytkownika przycisk.
     */
    public static Optional<ButtonType> ShowAlert(final Alert.AlertType type, final String text)
    {
        return ShowAlert(type, text, null);
    }

    /**
     * Instancja aktualnie działającej aplikacji.
     */
    public static Main Current;
    /**
     * Tytuł programu.
     */
    public static final String ProgramTitle = "To-Do Manager";
    /**
     * Format długich dat wykorzystywany w aplikacji.
     */
    public static final SimpleDateFormat LongDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
}
