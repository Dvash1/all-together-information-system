package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button forgot_password_button;

    @FXML
    private Label label_password;

    @FXML
    private Label label_username;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button secondaryButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    void emergency_button_press(ActionEvent event) {

    }

    @FXML
    void forgot_password(ActionEvent event) {

    }

    @FXML
    void login(ActionEvent event) throws IOException {
        String username = login_field.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
        root = loader.load();

        //TODO check in database that the user exists with the right password

        MainMenuController mainMenuController = loader.getController();
        System.out.println(username);
        mainMenuController.showUsername(username);

        SceneManager.switchScene("mainmenu.fxml",event);

    }
    @FXML
    void switchToPrimary(ActionEvent event) throws IOException {
        SceneManager.switchScene("primary.fxml",event);
    }
}